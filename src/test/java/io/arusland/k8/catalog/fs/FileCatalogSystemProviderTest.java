package io.arusland.k8.catalog.fs;

import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import io.arusland.k8.util.HashUtils;
import junit.framework.TestCase;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileCatalogSystemProviderTest extends TestCase {
    public void testProvider() {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, "c:\\");

        SearchObject rootObject = provider.getCatalog(source);

        assertTrue(rootObject.isCatalog());
        assertEquals(0, rootObject.getSize());

        System.out.println(rootObject);

        Iterable<SearchObject> objects = provider.getObjects(rootObject);

        for (SearchObject object : objects) {
            System.out.println("\t" + object);

            if (object.isCatalog()) {
                assertEquals(0, object.getSize());
            }
        }
    }

    public void testGetCatalogNormal() {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, "c:\\");

        SearchObject rootObject = provider.getCatalog(source);

        assertEquals("C", rootObject.getName());
        assertEquals("c:\\", rootObject.getPath());
        assertEquals(0L, rootObject.getSize());
        assertEquals(HashUtils.sha1Hex(rootObject.getPath().toLowerCase()), rootObject.getHash());
    }

    public void testGetCatalogWithCorrectLastActivePath() {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, "c:\\",
                "C:\\windows\\");

        SearchObject rootObject = provider.getCatalog(source);

        assertEquals("windows", rootObject.getName());
        assertEquals("C:\\windows", rootObject.getPath());
        assertEquals(0L, rootObject.getSize());
        assertEquals(HashUtils.sha1Hex(rootObject.getPath().toLowerCase()), rootObject.getHash());
    }

    public void testGetCatalogWithIncorrectLastActivePath() {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, "c:\\",
                "D:\\windows\\");

        try {
            provider.getCatalog(source);
        } catch (IllegalStateException e) {
            if (e.getMessage().equals("Directory 'D:\\windows' is not inner object of 'c:\\'")){
                return;
            }
        }

        fail("FileCatalogSystemProvider must throw exception");
    }

    public void testGetCatalogWithIncorrectLastActivePath2() {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, "c:\\",
                "C:\\windows\\foo222\boo333");

        SearchObject rootObject = provider.getCatalog(source);

        assertEquals("windows", rootObject.getName());
        assertEquals("C:\\windows", rootObject.getPath());
        assertEquals(0L, rootObject.getSize());
        assertEquals(HashUtils.sha1Hex(rootObject.getPath().toLowerCase()), rootObject.getHash());
    }
}
