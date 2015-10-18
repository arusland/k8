package io.arusland.k8.catalog.fs;

import io.arusland.k8.TestConfig;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceOwner;
import io.arusland.k8.source.SourceType;
import io.arusland.k8.util.HashUtils;
import junit.framework.TestCase;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileCatalogSystemProviderTest extends TestCase {
    public void testProvider() {
        final FileSkipProvider fileSkipper = new FileSkipProvider();
        final FileCatalogSystemProvider provider = new FileCatalogSystemProvider(fileSkipper);
        final SearchSource source = new SearchSource(SourceType.FileSystem, TestConfig.TEST_DATA_PATH, SourceOwner.DEFAULT);

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
        final SearchSource source = new SearchSource(SourceType.FileSystem, "c:\\", SourceOwner.DEFAULT);

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
                "C:\\windows\\", SourceOwner.DEFAULT);

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
                "D:\\windows\\", SourceOwner.DEFAULT);

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
                "C:\\windows\\foo222\boo333", SourceOwner.DEFAULT);

        SearchObject rootObject = provider.getCatalog(source);

        assertEquals("windows", rootObject.getName());
        assertEquals("C:\\windows", rootObject.getPath());
        assertEquals(0L, rootObject.getSize());
        assertEquals(HashUtils.sha1Hex(rootObject.getPath().toLowerCase()), rootObject.getHash());
    }
}
