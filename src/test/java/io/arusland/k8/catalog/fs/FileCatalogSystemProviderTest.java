package io.arusland.k8.catalog.fs;

import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileCatalogSystemProviderTest extends TestCase {
    public void testProvider(){
        FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, "c:\\");

        List<SearchObject> objects = provider.getObjects(source);

        assertEquals(1, objects.size());
        assertEquals(0, objects.get(0).getSize());

        System.out.println(objects.get(0));

        objects = provider.getObjects(objects.get(0));

        for (SearchObject object : objects){
            System.out.println("\t" + object);

            if (object.isCatalog()){
                assertEquals(0, object.getSize());
            }
        }
    }
}
