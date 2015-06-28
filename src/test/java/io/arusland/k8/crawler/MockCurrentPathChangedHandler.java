package io.arusland.k8.crawler;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by ruslan on 28.06.2015.
 */
public class MockCurrentPathChangedHandler implements Consumer<CatalogSystemCrawler> {
    private final List<String> paths = new LinkedList<>();

    @Override
    public void accept(CatalogSystemCrawler catalogSystemCrawler) {
        paths.add(catalogSystemCrawler.getCurrentCatalogPath());
    }

    public List<String> getPaths() {
        return paths;
    }
}