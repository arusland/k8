package io.arusland.k8.search;

import io.arusland.k8.catalog.CatalogSystemProvider;
import io.arusland.k8.catalog.ObjectType;
import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.SearchObject;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.search.SearchHit;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by ruslan on 27.06.2015.
 */
public class ResultParser {
    private final List<CatalogSystemProvider> providers;

    public ResultParser(List<CatalogSystemProvider> providers) {
        this.providers = Validate.notNull(providers).stream().collect(toList());
    }

    public SearchObject parse(SearchHit hit) {
        PropertyGetter propGetter = new PropertyGetter(hit.getFields());
        ObjectType type = propGetter.getObjectType();
        SearchObject result = null;

        for (CatalogSystemProvider provider : providers){
            if (provider.supports(type)){
                result = provider.getObject(propGetter);

                if (result != null){
                    return result;
                }
            }
        }

        throw new UnsupportedOperationException("Unsupported type: " + type);
    }
}
