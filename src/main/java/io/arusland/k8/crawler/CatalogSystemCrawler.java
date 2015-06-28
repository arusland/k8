package io.arusland.k8.crawler;

import io.arusland.k8.catalog.CatalogSystemProvider;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.search.SearchService;
import io.arusland.k8.source.SearchSource;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ruslan on 28.06.2015.
 */
public class CatalogSystemCrawler implements Runnable {
    private final static int WORKER_THREAD_MIN_COUNT = 1;
    private final SearchSource source;
    private final CatalogSystemProvider catalogProvider;
    private final SearchService searchService;
    private final List<Thread> threads = new LinkedList<>();
    private volatile Iterator<SearchObject> currentIterator;
    private final List<SearchObject> currentCatalogsBag = Collections.synchronizedList(new LinkedList<>());

    public CatalogSystemCrawler(SearchSource source, CatalogSystemProvider catalogSystemProvider,
                                SearchService searchService, int threadCount) {
        this.searchService = Validate.notNull(searchService);
        this.source = Validate.notNull(source);
        this.catalogProvider = Validate.notNull(catalogSystemProvider);

        Validate.isTrue(source.getType() == catalogSystemProvider.getType(), "source types mismatch");

        for (int i = 0; i < Math.max(threadCount, WORKER_THREAD_MIN_COUNT); i++) {
            threads.add(new Thread(new CrawlerWorker()));
        }
    }

    @Override
    public void run() {
        System.out.print("Starting crawler for: " + source);

        threads.stream().forEach(p -> {
            p.setDaemon(true);
            p.start();
        });

        final SearchObject rootCatalog = catalogProvider.getCatalog(source);
        searchService.index(rootCatalog);

        handleCatalog(rootCatalog);

        System.out.print("Stopped crawler for: " + source);
    }

    private void handleCatalog(SearchObject rootCatalog) {
        Validate.isTrue(rootCatalog.isCatalog(), "Must be catalog object");

        currentCatalogsBag.clear();

        final Iterator<SearchObject> objectsIterator = catalogProvider.getObjects(rootCatalog).iterator();
        setCurrentIterator(objectsIterator);

        while (objectsIterator.hasNext()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

        setCurrentIterator(null);

        if (!currentCatalogsBag.isEmpty()) {
            List<SearchObject> currentCatalogs = currentCatalogsBag
                    .stream()
                    .collect(Collectors.toList());

            currentCatalogs.forEach(p -> handleCatalog(p));
        }
    }

    public synchronized Iterator<SearchObject> getCurrentIterator() {
        return currentIterator;
    }

    public synchronized void setCurrentIterator(Iterator<SearchObject> currentIterator) {
        this.currentIterator = currentIterator;
    }

    private class CrawlerWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                Iterator<SearchObject> iterator = getCurrentIterator();

                if (iterator != null) {
                    SearchObject nextObject = iterator.next();

                    if (nextObject != null) {
                        if (nextObject.isCatalog()) {
                            currentCatalogsBag.add(nextObject);
                        }

                        searchService.index(nextObject);
                    }
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
