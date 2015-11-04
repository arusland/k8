package io.arusland.k8.catalog.db;

import io.arusland.k8.catalog.CatalogSystemProvider;
import io.arusland.k8.catalog.ObjectType;
import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;

import java.util.LinkedList;

/**
 * Created by ruslan on 27.06.2015.
 */
public class DatabaseCatalogSystemProvider implements CatalogSystemProvider {
    @Override
    public SourceType getType() {
        return SourceType.Database;
    }

    @Override
    public SearchObject getCatalog(SearchSource source) {
        return null;
    }

    @Override
    public Iterable<SearchObject> getObjects(SearchObject catalog) {
        return new LinkedList<>();
    }

    @Override
    public SearchObject getParent(SearchObject catalog) {
        return null;
    }

    @Override
    public SearchObject getObject(PropertyGetter props, SearchSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean supports(ObjectType type) {
        return type.toString().startsWith("DB_");
    }
}
