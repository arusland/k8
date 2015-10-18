package io.arusland.k8.search;


import io.arusland.k8.catalog.SearchObject;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;

/**
 * Created by ruslan on 18.10.2015.
 */
public class SearchFilter {
    public static final int DEFAULT_SEARCH_ROWS_COUNT = 25;
    private final String searchText;
    private final String[] searchIndecies;
    private final int pageSize;
    private final int startFrom;

    private SearchFilter(String searchText, int pageSize, int startFrom, String... searchIndecies) {
        this.searchText = searchText;
        this.pageSize = pageSize;
        this.startFrom = startFrom;
        this.searchIndecies = Validate.notNull(searchIndecies);

        Validate.isTrue(this.searchIndecies.length > 0);
    }

    public String[] getSearchIndecies() {
        return searchIndecies;
    }

    public String getSearchText() {
        return searchText;
    }

    public static SearchFilter createPublicSearch(String text){
        return new SearchFilter(text, DEFAULT_SEARCH_ROWS_COUNT, 0/*from*/,
                SearchObject.DEFAULT_INDEX);
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getStartFrom() {
        return startFrom;
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
                "searchText='" + searchText + '\'' +
                ", searchIndecies=" + Arrays.toString(searchIndecies) +
                ", pageSize=" + pageSize +
                ", startFrom=" + startFrom +
                '}';
    }
}
