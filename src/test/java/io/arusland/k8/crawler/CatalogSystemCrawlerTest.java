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
        assertTrue(contains(objects, "data"));
        assertTrue(contains(objects, "folder1"));
        assertTrue(contains(objects, "folder2"));
        assertTrue(contains(objects, "test1.txt"));
        assertTrue(contains(objects, "test2.txt"));
        assertTrue(contains(objects, "folder2.txt"));
        assertTrue(contains(objects, "folder2_1"));
        assertTrue(contains(objects, "folder2_1.avi"));

        final List<String> paths = pathHandler.getPaths();
        assertEquals(5, paths.size());
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder1"));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder2"));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder2\\folder2_1"));
        assertTrue(paths.contains(null));
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

        assertTrue(contains(objects, "folder2"));
        assertTrue(contains(objects, "folder2.txt"));
        assertTrue(contains(objects, "folder2_1"));
        assertTrue(contains(objects, "folder2_1.avi"));
        assertTrue(contains(objects, "test1.txt"));
        assertTrue(contains(objects, "test2.txt"));

        final List<String> paths = pathHandler.getPaths();
        assertEquals(3, paths.size());
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder2"));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder2\\folder2_1"));
        assertTrue(paths.contains(null));
    }

    private static boolean contains(List<SearchObject> objects, String name) {
        return objects.stream().anyMatch(p -> p.getName().equals(name));
    }
}
