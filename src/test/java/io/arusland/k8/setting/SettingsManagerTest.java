package io.arusland.k8.setting;

import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceOwner;
import io.arusland.k8.source.SourceType;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;

/**
 * Created by ruslan on 19.10.2015.
 */
public class SettingsManagerTest extends TestCase {

    public void testSave() {
        final File file = new File(System.getProperty("java.io.tmpdir"), "_tmp_k8settings.xml");

        if (file.exists()) {
            file.delete();
        }

        final SettingsManager settings = new SettingsManager(file);
        SearchSource source = new SearchSource(SourceType.FileSystem,
                "c:\\path1\\path2", SourceOwner.DEFAULT);

        List<SearchSource> sources = settings.loadSources();
        assertEquals(0, sources.size());

        sources.add(source);
        sources.add(source);

        settings.saveSources(sources);

        sources = settings.loadSources();

        assertEquals(2, sources.size());
        assertNotSame(source, sources.get(0));
        assertNotSame(source, sources.get(1));
        assertEquals(source, sources.get(0));
        assertEquals(source, sources.get(1));
    }

    public void testSaveWithLastActivePath() {
        final File file = new File(System.getProperty("java.io.tmpdir"), "_tmp_k8settings2.xml");

        if (file.exists()) {
            file.delete();
        }

        final SettingsManager settings = new SettingsManager(file);
        SearchSource source = new SearchSource(SourceType.Database,
                "c:\\path1\\path2", "c:\\path1\\path2\\path3", SourceOwner.DEFAULT);

        List<SearchSource> sources = settings.loadSources();
        assertEquals(0, sources.size());

        sources.add(source);

        settings.saveSources(sources);

        sources = settings.loadSources();

        assertEquals(1, sources.size());
        assertNotSame(source, sources.get(0));
        assertEquals(source, sources.get(0));
    }

    public void testSaveToExistingFile() {
        final File file = new File(System.getProperty("java.io.tmpdir"), "_tmp_k8settings3.xml");

        if (file.exists()) {
            file.delete();
        }

        final SettingsManager settings = new SettingsManager(file);
        SearchSource source = new SearchSource(SourceType.FileSystem,
                "c:\\path1\\path2", SourceOwner.DEFAULT);

        List<SearchSource> sources = settings.loadSources();
        assertEquals(0, sources.size());

        sources.add(source);
        settings.saveSources(sources);
        sources = settings.loadSources();

        assertEquals(1, sources.size());
        assertNotSame(source, sources.get(0));
        assertEquals(source, sources.get(0));

        assertTrue(file.exists());
        sources.clear();
        sources.add(source);
        settings.saveSources(sources);
        sources = settings.loadSources();

        assertEquals(1, sources.size());
        assertNotSame(source, sources.get(0));
        assertEquals(source, sources.get(0));
    }
}
