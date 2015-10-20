package io.arusland.k8.search;

import io.arusland.k8.source.SourceOwner;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;

/**
 * Created by ruslan on 18.10.2015.
 */
public class SearchFilter {
    public static final int DEFAULT_SEARCH_ROWS_COUNT = 25;
    private final String searchText;
    private final SourceOwner[] sourceOwners;
    private final int pageSize;
    private final int startFrom;

    private SearchFilter(String searchText, int pageSize, int startFrom, SourceOwner... sourceOwners) {
        this.searchText = searchText;
        this.pageSize = pageSize;
        this.startFrom = startFrom;
        this.sourceOwners = Validate.notNull(sourceOwners);

        Validate.isTrue(this.sourceOwners.length > 0);
    }

    public SourceOwner[] getSearchOwners() {
        return sourceOwners;
    }

    public String[] getSearchIndecies() {
        return Arrays.stream(sourceOwners)
                .map(p -> p.getName())
                .toArray(size -> new String[size]);
    }

    public String getSearchText() {
        return searchText;
    }

    public static SearchFilter createPublicSearch(String text) {
        return new SearchFilter(text, DEFAULT_SEARCH_ROWS_COUNT, 0/*from*/,
                SourceOwner.DEFAULT);
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
                ", sourceOwners=" + Arrays.toString(sourceOwners) +
                ", pageSize=" + pageSize +
                ", startFrom=" + startFrom +
                '}';
    }
}
