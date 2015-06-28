package io.arusland.k8.search;

import io.arusland.k8.catalog.SearchObject;

import java.util.List;

/**
 * Created by ruslan on 28.06.2015.
 */
public interface SearchService {
    List<SearchObject> search(String searchText);

    void index(SearchObject object);
}
