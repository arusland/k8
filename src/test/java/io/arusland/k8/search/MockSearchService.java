package io.arusland.k8.search;

import io.arusland.k8.catalog.SearchObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruslan on 28.06.2015.
 */
public class MockSearchService implements SearchService {
    private final List<SearchObject> indexedObjects = new LinkedList<>();

    @Override
    public List<SearchObject> search(SearchFilter filter) {
        return new LinkedList<>();
    }

    @Override
    public List<SearchObject> search(String searchText) {
        return new LinkedList<>();
    }

    @Override
    public void index(SearchObject object) {
        indexedObjects.add(object);
    }

    public List<SearchObject> getIndexedObjects() {
        return indexedObjects;
    }
}
