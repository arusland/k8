package io.arusland.k8.catalog.fs;

import io.arusland.k8.catalog.CatalogSystemProvider;
import io.arusland.k8.catalog.ObjectType;
import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.catalog.fs.format.TextFileSearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileCatalogSystemProvider implements CatalogSystemProvider {
    private static Logger logger = LoggerFactory.getLogger(FileCatalogSystemProvider.class);
    private final FileSkipProvider fileSkipper;

    @Autowired
    public FileCatalogSystemProvider(FileSkipProvider fileSkipper) {
        this.fileSkipper = Validate.notNull(fileSkipper, "fileSkipper");
    }

    @Override
    public SourceType getType() {
        return SourceType.FileSystem;
    }

    /**
     * Returns root catalog by source
     */
    @Override
    public SearchObject getCatalog(SearchSource source) {
        Validate.notNull(source, "source");
        Validate.isTrue(source.getType() == SourceType.FileSystem, "source must be SourceType.FileSystem");

        final File rootFile = new File(source.getPath());

        if (!rootFile.exists()) {
            throw new IllegalStateException("Directory not found: " + rootFile);
        }

        if (source.hasLastActiveCatalog()) {
            File file = new File(source.getLastActiveCatalog());

            if (!file.getAbsolutePath().toLowerCase().startsWith(rootFile.getAbsolutePath().toLowerCase())) {
                throw new IllegalStateException(String.format("Directory '%s' is not inner object of '%s'",
                        file, rootFile));
            }

            while (file != null && !file.exists()) {
                file = file.getParentFile();
            }

            if (file != null) {
                try {
                    return createObject(file);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        try {
            return createObject(rootFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Iterable<SearchObject> getObjects(SearchObject catalog) {
        FileObjectIterator iterator = new FileObjectIterator(catalog);

        return new Iterable<SearchObject>() {
            @Override
            public Iterator<SearchObject> iterator() {
                return iterator;
            }
        };
    }

    @Override
    public SearchObject getParent(SearchObject catalog) {
        Validate.isTrue(catalog.getSourceType() == SourceType.FileSystem, "source must be SourceType.FileSystem");

        FileSearchObject object = (FileSearchObject) catalog;
        File parent = object.getFile().getParentFile();

        if (parent != null) {
            try {
                return createObject(parent);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        return null;
    }

    @Override
    public SearchObject getObject(PropertyGetter props) {
        ObjectType type = props.getObjectType();

        switch (type) {
            case FILE_TEXT:
                return new TextFileSearchObject(props);
            default:
                return new FileSearchObject(props);
        }
    }

    @Override
    public boolean supports(ObjectType type) {
        return type.toString().startsWith("FILE_");
    }

    private FileSearchObject createObject(File file) throws IOException {
        ObjectType type = FileTypeHelper.getObjectType(file);

        switch (type) {
            case FILE_TEXT:
                return new TextFileSearchObject(file);
            default:
                return new FileSearchObject(file);
        }
    }

    private class FileObjectIterator implements Iterator<SearchObject> {
        private final File[] files;
        private int currentIndex;

        public FileObjectIterator(SearchObject catalog) {
            Validate.isInstanceOf(FileSearchObject.class, catalog);
            Validate.isTrue(catalog.isCatalog(), "Method supports only catalog objects");

            File file = ((FileSearchObject) catalog).getFile();
            this.files = file.listFiles(file1 -> !fileSkipper.test(file1));
        }

        @Override
        public boolean hasNext() {
            return currentIndex < files.length;
        }

        @Override
        public SearchObject next() {
            File file = null;

            synchronized (files) {
                if (currentIndex < files.length) {
                    file = files[currentIndex];

                    currentIndex++;
                }
            }

            if (file != null) {
                try {
                    return createObject(file);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            return null;
        }
    }
}
