package io.arusland.k8.source;

import io.arusland.k8.util.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruslan on 27.06.2015.
 */
public class SearchSource {
    private final SourceType type;
    private final String path;
    private final List<String> owners = new LinkedList<>();
    private String lastActiveCatalog;

    public SearchSource(SourceType type, String path, String lastActiveCatalog) {
        this.type = Validate.notNull(type);
        this.path = PathUtils.normalizePath(Validate.notBlank(path));
        this.lastActiveCatalog = PathUtils.normalizePath(lastActiveCatalog);
    }

    public SearchSource(SourceType type, String path) {
        this(type, path, null);
    }

    public SourceType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public Iterable<String> getOwners() {
        return owners;
    }

    public boolean isPublic() {
        return owners.isEmpty();
    }

    public String getLastActiveCatalog() {
        return lastActiveCatalog;
    }

    public boolean hasLastActiveCatalog() {
        return StringUtils.isNotBlank(lastActiveCatalog);
    }

    @Override
    public String toString() {
        return "SearchSource{" +
                "type=" + type +
                ", path='" + path + '\'' +
                ", owners=" + owners +
                ", lastActiveCatalog='" + lastActiveCatalog + '\'' +
                '}';
    }
}
