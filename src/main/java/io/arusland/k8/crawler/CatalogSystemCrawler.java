package io.arusland.k8.crawler;

import io.arusland.k8.catalog.CatalogSystemProvider;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.search.SearchService;
import io.arusland.k8.source.SearchSource;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by ruslan on 28.06.2015.
 */
public class CatalogSystemCrawler implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CatalogSystemCrawler.class);
    private final static int WORKER_THREAD_MIN_COUNT = 1;
    private final SearchSource source;
    private final CatalogSystemProvider catalogProvider;
    private final SearchService searchService;
    private final List<Thread> threads = new LinkedList<>();
    private final Consumer<CatalogSystemCrawler> currentPathChangedHandler;
    private volatile Iterator<SearchObject> currentIterator;
    private final List<SearchObject> currentCatalogsBag = Collections.synchronizedList(new LinkedList<>());
    private String currentCatalogPath;

    public CatalogSystemCrawler(SearchSource source, CatalogSystemProvider catalogSystemProvider,
                                SearchService searchService, Consumer<CatalogSystemCrawler> currentPathChangedHandler,
                                int threadCount) {
        this.currentPathChangedHandler = currentPathChangedHandler;
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
        logger.debug("Started crawler for: " + source);

        threads.stream().forEach(p -> {
            p.setDaemon(true);
            p.start();
        });

        SearchObject rootCatalog = catalogProvider.getCatalog(source);
        searchService.index(rootCatalog);

        handleCatalog(rootCatalog);

        if (source.hasLastActiveCatalog() && !rootCatalog.isPathEquals(source)) {
            SearchObject parent = catalogProvider.getParent(rootCatalog);

            while (parent != null) {
                Iterator<SearchObject> iterator = catalogProvider.getObjects(parent).iterator();

                // search current catalog and start indexing after it
                while (iterator.hasNext()) {
                    SearchObject nextObject = iterator.next();

                    if (nextObject != null) {
                        if (nextObject.isCatalog()) {
                            if (nextObject.getHash().equals(rootCatalog.getHash())) {
                                break;
                            }
                        } else {
                            // oops! we cannot find current catalog, but we found file
                            // index it and continue indexing all the rest files
                            searchService.index(nextObject);
                            break;
                        }
                    }
                }

                // rest the rest catalog & files
                handleIterator(iterator);

                if (parent.isPathEquals(source)) {
                    break;
                }

                // go catalog up
                rootCatalog = parent;
                parent = catalogProvider.getParent(parent);
            }
        }

        setCurrentCatalogPath(null);

        logger.debug("Stopped crawler for: " + source);
    }

    private void handleCatalog(SearchObject rootCatalog) {
        Validate.isTrue(rootCatalog.isCatalog(), "Must be catalog object");

        setCurrentCatalogPath(rootCatalog.getPath());

        final Iterator<SearchObject> objectsIterator = catalogProvider.getObjects(rootCatalog).iterator();

        handleIterator(objectsIterator);
    }

    private void handleIterator(Iterator<SearchObject> objectsIterator) {
        currentCatalogsBag.clear();

        setCurrentIterator(objectsIterator);

        while (objectsIterator.hasNext()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
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

    public synchronized String getCurrentCatalogPath() {
        return currentCatalogPath;
    }

    public void setCurrentCatalogPath(String currentCatalogPath) {
        synchronized (this) {
            this.currentCatalogPath = currentCatalogPath;
        }

        if (currentPathChangedHandler != null) {
            currentPathChangedHandler.accept(this);
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
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
