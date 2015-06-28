package io.arusland.k8.catalog.fs;

import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import junit.framework.TestCase;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileCatalogSystemProviderTest extends TestCase {
    public void testProvider(){
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, "c:\\");

        SearchObject rootObject = provider.getCatalog(source);

        assertTrue(rootObject.isCatalog());
        assertEquals(0, rootObject.getSize());

        System.out.println(rootObject);

        Iterable<SearchObject> objects = provider.getObjects(rootObject);

        for (SearchObject object : objects){
            System.out.println("\t" + object);

            if (object.isCatalog()){
                assertEquals(0, object.getSize());
            }
        }
    }
}
