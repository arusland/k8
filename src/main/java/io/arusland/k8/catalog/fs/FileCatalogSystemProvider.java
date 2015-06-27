package io.arusland.k8.catalog.fs;

import io.arusland.k8.catalog.CatalogSystemProvider;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileCatalogSystemProvider implements CatalogSystemProvider {
    @Override
    public SourceType getSourceType() {
        return SourceType.FileSystem;
    }

    @Override
    public List<SearchObject> getObjects(SearchSource source) {
        Validate.notNull(source, "source");
        Validate.isTrue(source.getType() == SourceType.FileSystem, "source must be SourceType.FileSystem");

        File file = new File(source.getPath());
        LinkedList<SearchObject> result = new LinkedList<>();

        try {
            result.add(new FileSearchObject(file));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return result;
    }

    @Override
    public List<SearchObject> getObjects(SearchObject catalog) {
        Validate.isInstanceOf(FileSearchObject.class, catalog);
        Validate.isTrue(catalog.isCatalog(), "Method supports only catalog objects");

        File file = ((FileSearchObject) catalog).getFile();
        LinkedList<SearchObject> result = new LinkedList<>();

        if (file.exists()) {
            File[] files = file.listFiles();

            for (File fileChild : files) {
                try {
                    result.add(new FileSearchObject(fileChild));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
