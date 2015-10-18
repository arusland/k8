package io.arusland.k8.search;

import io.arusland.k8.catalog.SearchObject;

import java.util.List;

/**
 * Created by ruslan on 28.06.2015.
 */
public interface SearchService {
    /**
     * Search objects by filter
     */
    List<SearchObject> search(SearchFilter filter);

    /**
     * Search objects in public index with default settings.
     */
    List<SearchObject> search(String searchText);

    /**
     * Index specified object.
     */
    void index(SearchObject object);

    /**
     * Explicitly flush all indices.
     */
    void flush();
}
