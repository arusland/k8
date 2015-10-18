package io.arusland.k8.crawler;

import io.arusland.k8.TestConfig;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.catalog.fs.FileCatalogSystemProvider;
import io.arusland.k8.catalog.fs.FileSkipProvider;
import io.arusland.k8.search.ElasticSearchService;
import io.arusland.k8.search.MockSearchService;
import io.arusland.k8.search.ResultParser;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceOwner;
import io.arusland.k8.source.SourceType;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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
        final SearchSource source = new SearchSource(SourceType.FileSystem, TestConfig.TEST_DATA_PATH, SourceOwner.DEFAULT);
        final MockSearchService service = new MockSearchService();
        final CatalogSystemCrawler crawler = new CatalogSystemCrawler(source, provider,
                service, pathHandler, THREAD_COUNT);
        final Thread thread = new Thread(crawler);

        thread.start();
        thread.join();

        List<SearchObject> objects = service.getIndexedObjects();
        assertEquals(10, objects.size());
        assertTrue(contains(objects, "data"));
        assertTrue(contains(objects, "folder1"));
        assertTrue(contains(objects, "folder2"));
        assertTrue(contains(objects, "folder3"));
        assertTrue(contains(objects, "test1.txt"));
        assertTrue(contains(objects, "test2.txt"));
        assertTrue(contains(objects, "folder2.txt"));
        assertTrue(contains(objects, "folder2_1"));
        assertTrue(contains(objects, "folder2_1.avi"));
        assertTrue(contains(objects, "drivers1.xml"));

        final List<String> paths = pathHandler.getPaths();
        assertEquals(6, paths.size());
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder1"));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder2"));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder3"));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder2\\folder2_1"));
        assertTrue(paths.contains(null));
    }

    public void testSourceWithLastActiveCatalogPath() throws InterruptedException {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final MockCurrentPathChangedHandler pathHandler = new MockCurrentPathChangedHandler();
        final SearchSource source = new SearchSource(SourceType.FileSystem, TestConfig.TEST_DATA_PATH,
                TestConfig.TEST_DATA_PATH + "\\folder2\\", SourceOwner.DEFAULT);

        final MockSearchService service = new MockSearchService();
        final CatalogSystemCrawler crawler = new CatalogSystemCrawler(source, provider,
                service, pathHandler, THREAD_COUNT);
        final Thread thread = new Thread(crawler);

        thread.start();
        thread.join();

        final List<SearchObject> objects = service.getIndexedObjects();
        assertEquals(8, objects.size());

        assertTrue(contains(objects, "folder2"));
        assertTrue(contains(objects, "folder2.txt"));
        assertTrue(contains(objects, "folder2_1"));
        assertTrue(contains(objects, "folder3"));
        assertTrue(contains(objects, "folder2_1.avi"));
        assertTrue(contains(objects, "test1.txt"));
        assertTrue(contains(objects, "test2.txt"));
        assertTrue(contains(objects, "drivers1.xml"));

        final List<String> paths = pathHandler.getPaths();
        assertEquals(4, paths.size());
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder2"));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder2\\folder2_1"));
        assertTrue(paths.contains(TestConfig.TEST_DATA_PATH + "\\folder3"));
        assertTrue(paths.contains(null));
    }

    public void testRealCatalogSystemCrawler() throws InterruptedException {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final MockCurrentPathChangedHandler pathHandler = new MockCurrentPathChangedHandler();
        final SearchSource source = new SearchSource(SourceType.FileSystem, TestConfig.TEST_DATA_PATH, SourceOwner.DEFAULT);
        final ResultParser resultParser = new ResultParser(Arrays.asList(provider));
        final ElasticSearchService service = new ElasticSearchService(resultParser);
        final CatalogSystemCrawler crawler = new CatalogSystemCrawler(source, provider,
                service, pathHandler, THREAD_COUNT);
        final Thread thread = new Thread(crawler);

        thread.start();
        thread.join();

        List<SearchObject> result1 = service.search("Limousine");
        assertEquals(1, result1.size());

        List<SearchObject> result2 = service.search("thisId");
        assertEquals(1, result2.size());
    }

    private static boolean contains(List<SearchObject> objects, String name) {
        return objects.stream().anyMatch(p -> p.getName().equals(name));
    }
}
