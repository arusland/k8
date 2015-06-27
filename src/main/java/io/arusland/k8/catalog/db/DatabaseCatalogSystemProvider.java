package io.arusland.k8.catalog.db;

import io.arusland.k8.catalog.CatalogSystemProvider;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruslan on 27.06.2015.
 */
public class DatabaseCatalogSystemProvider implements CatalogSystemProvider {
    @Override
    public SourceType getSourceType() {
        return SourceType.Database;
    }

    @Override
    public List<SearchObject> getObjects(SearchSource source) {
        return new LinkedList<>();
    }

    @Override
    public List<SearchObject> getObjects(SearchObject catalog) {
        return new LinkedList<>();
    }
}
