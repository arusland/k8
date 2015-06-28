package io.arusland.k8.crawler;

import io.arusland.k8.catalog.fs.FileCatalogSystemProvider;
import io.arusland.k8.catalog.fs.FileSkipProvider;
import io.arusland.k8.search.SearchService;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import junit.framework.TestCase;

/**
 * Created by ruslan on 28.06.2015.
 */
public class CatalogSystemCrawlerTest extends TestCase {
    private static final int THREAD_COUNT = 5;

    public void test() throws InterruptedException {

        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, "d:\\WORK\\MyProjects\\k8\\src\\test\\data\\");
        SearchService service = new SearchService();
        CatalogSystemCrawler crawler = new CatalogSystemCrawler(source, provider, service, THREAD_COUNT);
        Thread thread = new Thread(crawler);

        thread.start();
        thread.join();

        service.search("test");
    }
}
