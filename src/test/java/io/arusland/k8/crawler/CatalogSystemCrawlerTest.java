package io.arusland.k8.crawler;

import io.arusland.k8.TestConfig;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.catalog.fs.FileCatalogSystemProvider;
import io.arusland.k8.catalog.fs.FileSkipProvider;
import io.arusland.k8.search.MockSearchService;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ruslan on 28.06.2015.
 */
public class CatalogSystemCrawlerTest extends TestCase {
    private static Logger logger = LoggerFactory.getLogger(CatalogSystemCrawlerTest.class);
    private static final int THREAD_COUNT = 5;

    public void testIndexingPureSource() throws InterruptedException {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final MockCurrentPathChangedHandler pathHandler = new MockCurrentPathChangedHandler();
        final SearchSource source = new SearchSource(SourceType.FileSystem, TestConfig.TEST_DATA_PATH);
        final MockSearchService service = new MockSearchService();
        final CatalogSystemCrawler crawler = new CatalogSystemCrawler(source, provider,
                service, pathHandler, THREAD_COUNT);
        final Thread thread = new Thread(crawler);

        thread.start();
        thread.join();

        List<SearchObject> objects = service.getIndexedObjects();
        assertEquals(8, objects.size());
        assertEquals("data", objects.get(0).getName());
        assertEquals("folder1", objects.get(1).getName());
        assertEquals("folder2", objects.get(2).getName());
        assertEquals("test1.txt", objects.get(3).getName());
        assertEquals("test2.txt", objects.get(4).getName());
        assertEquals("folder2.txt", objects.get(5).getName());
        assertEquals("folder2_1", objects.get(6).getName());
        assertEquals("folder2_1.avi", objects.get(7).getName());

        final List<String> paths = pathHandler.getPaths();
        assertEquals(5, paths.size());
        assertEquals(TestConfig.TEST_DATA_PATH, paths.get(0));
        assertEquals(TestConfig.TEST_DATA_PATH + "\\folder1", paths.get(1));
        assertEquals(TestConfig.TEST_DATA_PATH + "\\folder2", paths.get(2));
        assertEquals(TestConfig.TEST_DATA_PATH + "\\folder2\\folder2_1", paths.get(3));
        assertEquals(null, paths.get(4));
    }

    public void testSourceWithLastActiveCatalogPath() throws InterruptedException {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final MockCurrentPathChangedHandler pathHandler = new MockCurrentPathChangedHandler();
        final SearchSource source = new SearchSource(SourceType.FileSystem, TestConfig.TEST_DATA_PATH,
                TestConfig.TEST_DATA_PATH + "\\folder2\\");

        final MockSearchService service = new MockSearchService();
        final CatalogSystemCrawler crawler = new CatalogSystemCrawler(source, provider,
                service, pathHandler, THREAD_COUNT);
        final Thread thread = new Thread(crawler);

        thread.start();
        thread.join();

        final List<SearchObject> objects = service.getIndexedObjects();
        assertEquals(6, objects.size());
        assertEquals("folder2", objects.get(0).getName());
        assertEquals("folder2.txt", objects.get(1).getName());
        assertEquals("folder2_1", objects.get(2).getName());
        assertEquals("folder2_1.avi", objects.get(3).getName());
        assertEquals("test1.txt", objects.get(4).getName());
        assertEquals("test2.txt", objects.get(5).getName());

        final List<String> paths = pathHandler.getPaths();
        assertEquals(3, paths.size());
        assertEquals(TestConfig.TEST_DATA_PATH + "\\folder2", paths.get(0));
        assertEquals(TestConfig.TEST_DATA_PATH + "\\folder2\\folder2_1", paths.get(1));
        assertEquals(null, paths.get(2));
    }
}
