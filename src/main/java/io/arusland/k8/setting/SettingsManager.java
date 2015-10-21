package io.arusland.k8.setting;

import io.arusland.k8.dto.SearchSourceDto;
import io.arusland.k8.source.SearchSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

                result.addAll(wrapper.getSearchSources()
                        .stream()
                        .map(p -> p.toEntity())
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
                    .map(p -> SearchSourceDto.fromEntity(p))
                    .collect(Collectors.toList()));

            m.marshal(wrapper, file);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void update(SearchSource source){
        List<SearchSource> sources = loadSources();
        Optional<SearchSource> oldSource = sources.stream()
                .filter(p -> p.getId().equals(source.getId()))
                .findFirst();

        if (oldSource.isPresent()){
            sources.remove(oldSource.get());
        }

        sources.add(source);
        saveSources(sources);
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

    @XmlRootElement(name = "settings")
    public static class SettingsWrapper {
        private List<SearchSourceDto> searchSource = new LinkedList<>();

        @XmlElement(name = "searchSource")
        public List<SearchSourceDto> getSearchSources() {
            return searchSource;
        }

        public void setSearchSource(List<SearchSourceDto> searchSource) {
            this.searchSource = searchSource;
        }
    }
}
