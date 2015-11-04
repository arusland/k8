package io.arusland.k8.search;

import io.arusland.k8.catalog.CatalogSystemProvider;
import io.arusland.k8.catalog.ObjectType;
import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.setting.SettingsManager;
import io.arusland.k8.source.SearchSource;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.search.SearchHit;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by ruslan on 27.06.2015.
 */
public class ResultParser {
    private final List<CatalogSystemProvider> providers;

    public ResultParser(final List<CatalogSystemProvider> providers) {
        this.providers = Validate.notNull(providers)
                .stream()
                .collect(toList());
    }

    public SearchObject parse(SearchHit hit) {
        PropertyGetter propGetter = new PropertyGetter(hit.getFields());
        ObjectType type = propGetter.getObjectType();
        SearchObject result = null;

        for (CatalogSystemProvider provider : providers){
            if (provider.supports(type)){
                result = provider.getObject(propGetter, findSourceByIndex(propGetter.getIndex()));

                if (result != null){
                    return result;
                }
            }
        }

        throw new UnsupportedOperationException("Unsupported type: " + type);
    }

    @NotNull
    public SearchSource findSourceByIndex(String index){
        // TODO: optimize
        Optional<SearchSource> source = SettingsManager.getInstance()
                .loadSources()
                .stream()
                .filter(p -> p.getIndexName().equals(index))
                .findFirst();

        if (source.isPresent()){
            return source.get();
        }

        throw new IllegalStateException("Index not found: " + index);
    }
}
