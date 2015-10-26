package io.arusland.k8.crawler;

import io.arusland.k8.catalog.fs.FileCatalogSystemProvider;
import io.arusland.k8.search.SearchService;
import io.arusland.k8.util.ThreadUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ruslan on 26.10.2015.
 */
@Service
public class CrawlerRunner {
    private static final Logger logger = LoggerFactory.getLogger(CrawlerRunner.class);
    private static final int FIRST_PAUSE_TIMEOUT = 5000;
    private static final int WORKER_TIMEOUT = 500;
    private final SearchService searchService;
    private final FileCatalogSystemProvider fileCatalogSystemProvider;

    @Autowired
    public CrawlerRunner(SearchService searchService, FileCatalogSystemProvider fileCatalogSystemProvider){
        this.fileCatalogSystemProvider = Validate.notNull(fileCatalogSystemProvider);
        this.searchService = Validate.notNull(searchService);
        new Thread(() -> mainThreadWorker()).start();
    }

    private void mainThreadWorker(){
        ThreadUtil.sleep(FIRST_PAUSE_TIMEOUT);
        logger.debug("CrawlerRunner started");

        while (true){
            ThreadUtil.sleep(WORKER_TIMEOUT);

            // TODO: run crawler on the source
        }
    }
}
