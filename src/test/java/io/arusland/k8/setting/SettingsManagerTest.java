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
    private static String FILE_NAME = "k8settingsTest.xml";

    public void testSave() {
        final SettingsManager settings = new SettingsManager(new File(System.getProperty("user.home"), FILE_NAME));

        if (settings.getSettingsFile().exists()) {
            settings.getSettingsFile().delete();
        }

        SearchSource source1 = new SearchSource(SourceType.FileSystem,
                "c:\\path1\\path2", SourceOwner.DEFAULT);
        SearchSource source2 = new SearchSource(SourceType.Database,
                "c:\\path1\\path66", SourceOwner.DEFAULT);

        List<SearchSource> sources = settings.loadSources();
        assertEquals(0, sources.size());

        sources.add(source1);
        sources.add(source2);

        settings.saveSources(sources);

        sources = settings.loadSources();

        assertEquals(2, sources.size());
        assertNotSame(source1, sources.get(0));
        assertNotSame(source2, sources.get(1));
        assertEquals(source1, sources.get(0));
        assertEquals(source2, sources.get(1));
    }

    public void testSaveWithLastActivePath() {
        final SettingsManager settings = new SettingsManager(new File(System.getProperty("user.home"), FILE_NAME));

        if (settings.getSettingsFile().exists()) {
            settings.getSettingsFile().delete();
        }

        SearchSource source1 = new SearchSource(SourceType.Database,
                "c:\\path1\\path2", "c:\\path1\\path2\\path3", SourceOwner.DEFAULT);

        List<SearchSource> sources = settings.loadSources();
        assertEquals(0, sources.size());

        sources.add(source1);

        settings.saveSources(sources);

        sources = settings.loadSources();

        assertEquals(1, sources.size());
        assertNotSame(source1, sources.get(0));
        assertEquals(source1, sources.get(0));
    }

    public void testSaveToExistingFile() {
        final SettingsManager settings = new SettingsManager(new File(System.getProperty("user.home"), FILE_NAME));

        if (settings.getSettingsFile().exists()) {
            settings.getSettingsFile().delete();
        }

        SearchSource source1 = new SearchSource(SourceType.FileSystem,
                "c:\\path1\\path2", SourceOwner.DEFAULT);

        List<SearchSource> sources = settings.loadSources();
        assertEquals(0, sources.size());

        sources.add(source1);
        settings.saveSources(sources);
        sources = settings.loadSources();

        assertEquals(1, sources.size());
        assertNotSame(source1, sources.get(0));
        assertEquals(source1, sources.get(0));

        assertTrue(settings.getSettingsFile().exists());
        sources.clear();

        SearchSource source2 = new SearchSource(SourceType.Database,
                "c:\\path1\\path3", SourceOwner.DEFAULT);

        sources.add(source2);
        settings.saveSources(sources);
        sources = settings.loadSources();

        assertEquals(1, sources.size());
        assertNotSame(source2, sources.get(0));
        assertEquals(source2, sources.get(0));
    }
}
