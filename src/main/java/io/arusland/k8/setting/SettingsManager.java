package io.arusland.k8.setting;

import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceOwner;
import io.arusland.k8.source.SourceType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ruslan on 19.10.2015.
 */
public class SettingsManager {
    private final static String DEFAULT_FILE_NAME = "k8settings.xml";
    private final File file;
    private static SettingsManager instance;
    private final static Object lock = new Object();

    SettingsManager(File file) {
        this.file = file;
    }

    public List<SearchSource> loadSources() {
        final List<SearchSource> result = new LinkedList<>();

        if (file.exists()) {
            try {
                JAXBContext context = JAXBContext.newInstance(SettingsWrapper.class);
                Unmarshaller um = context.createUnmarshaller();

                SettingsWrapper wrapper = (SettingsWrapper) um.unmarshal(file);

                result.addAll(wrapper.getSearchSources().stream().map(SettingsManager::toEntity)
                        .collect(Collectors.toList()));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        return result;
    }

    public void saveSources(final List<SearchSource> sources) {
        try {
            JAXBContext context = JAXBContext.newInstance(SettingsWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            SettingsWrapper wrapper = file.exists() ? (SettingsWrapper) um.unmarshal(file)
                    : new SettingsWrapper();

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            wrapper.setSearchSource(sources.stream()
                    .map(p -> toConfig(p))
                    .collect(Collectors.toList()));

            m.marshal(wrapper, file);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    File getSettingsFile() {
        return file;
    }

    public static SettingsManager getInstance() {
        return getInstance(DEFAULT_FILE_NAME);
    }

    static SettingsManager getInstance(String fileName) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    SettingsManager tmp = new SettingsManager(
                            new File(System.getProperty("user.home"), fileName));

                    instance = tmp;
                }
            }
        }

        return instance;
    }

    private static SearchSource toEntity(SearchSourceConfig source) {
        return new SearchSource(SourceType.valueOf(source.getType()),
                source.getPath(),
                source.getLastActiveCatalog(),
                new SourceOwner(source.getOwner()), source.getId());
    }

    private static SearchSourceConfig toConfig(SearchSource source) {
        return new SearchSourceConfig(source.getType().toString(), source.getPath(),
                source.getOwner().getName(), source.getLastActiveCatalog(), source.getId());
    }

    @XmlRootElement(name = "settings")
    public static class SettingsWrapper {
        private List<SearchSourceConfig> searchSource = new LinkedList<>();

        @XmlElement(name = "searchSource")
        public List<SearchSourceConfig> getSearchSources() {
            return searchSource;
        }

        public void setSearchSource(List<SearchSourceConfig> searchSource) {
            this.searchSource = searchSource;
        }
    }

    public static class SearchSourceConfig {
        private Long id;
        private String type;
        private String path;
        private String owner;
        private String lastActiveCatalog;

        public SearchSourceConfig() {
        }

        public SearchSourceConfig(String type, String path, String owner, String lastActiveCatalog, Long id) {
            this.id = id;
            this.type = type;
            this.path = path;
            this.owner = owner;
            this.lastActiveCatalog = lastActiveCatalog;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getLastActiveCatalog() {
            return lastActiveCatalog;
        }

        public void setLastActiveCatalog(String lastActiveCatalog) {
            this.lastActiveCatalog = lastActiveCatalog;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
