package io.arusland.k8.search;

import io.arusland.k8.TestConfig;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.catalog.fs.FileCatalogSystemProvider;
import io.arusland.k8.catalog.fs.FileSkipProvider;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by ruslan on 27.06.2015.
 */
public class SearchServiceTest extends TestCase {
    public void testElasticSearchService(){
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, TestConfig.TEST_DATA_PATH);
        ResultParser resultParser = new ResultParser(Arrays.asList(provider));
        SearchService service = new ElasticSearchService(resultParser);


        SearchObject rootObject = provider.getCatalog(source);
        Iterator<SearchObject> files = provider.getObjects(rootObject).iterator();
        int count = 0;

        while (files.hasNext()) {
            service.index(files.next());
            count++;
        }

        assertEquals(5, count);

        service.search("test");
        service.flush();
    }
}
