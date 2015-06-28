package io.arusland.k8.search;

import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.catalog.fs.FileCatalogSystemProvider;
import io.arusland.k8.catalog.fs.FileSkipProvider;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import junit.framework.TestCase;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ruslan on 27.06.2015.
 */
public class SearchServiceTest extends TestCase {
    public void test(){
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, "d:\\WORK\\MyProjects\\k8\\src\\test\\data\\");
        SearchService service = new SearchService();

        List<SearchObject> objects = provider.getObjects(source);
        List<SearchObject> files = provider.getObjects(objects.get(0))
                .stream()
                .filter(p -> !p.isCatalog())
                .collect(Collectors.toList());

        assertEquals(2, files.size());

        files.forEach(p -> service.index(p));

        service.search("test");
    }
}
