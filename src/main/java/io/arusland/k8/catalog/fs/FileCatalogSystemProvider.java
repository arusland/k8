package io.arusland.k8.catalog.fs;

import io.arusland.k8.catalog.CatalogSystemProvider;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileCatalogSystemProvider implements CatalogSystemProvider {
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

        final File file = new File(source.getPath());

        try {
            return new FileSearchObject(file);
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

    private class FileObjectIterator implements Iterator<SearchObject> {
        private final File[] files;
        private int currentIndex = -1;

        public FileObjectIterator(SearchObject catalog) {
            Validate.isInstanceOf(FileSearchObject.class, catalog);
            Validate.isTrue(catalog.isCatalog(), "Method supports only catalog objects");

            File file = ((FileSearchObject) catalog).getFile();
            this.files = file.listFiles(file1 -> !fileSkipper.test(file1));
        }

        @Override
        public boolean hasNext() {
            return (currentIndex + 1) < files.length;
        }

        @Override
        public SearchObject next() {
            File file = null;

            synchronized (files) {
                currentIndex++;

                if (currentIndex < files.length) {
                    file = files[currentIndex];
                }
            }

            if (file != null) {
                try {
                    return new FileSearchObject(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
