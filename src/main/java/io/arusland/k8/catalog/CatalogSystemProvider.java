package io.arusland.k8.catalog;

import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;

import java.util.List;

/**
 * Created by ruslan on 27.06.2015.
 */
public interface CatalogSystemProvider {
    SourceType getSourceType();

    List<SearchObject> getObjects(SearchSource source);

    List<SearchObject> getObjects(SearchObject catalog);
}
