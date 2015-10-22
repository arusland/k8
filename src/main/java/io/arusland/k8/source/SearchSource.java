package io.arusland.k8.source;

import io.arusland.k8.util.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Random;

/**
 * Created by ruslan on 27.06.2015.
 */
public class SearchSource {
    private final Long id;
    private final SourceType type;
    private final String path;
    private final SourceOwner owner;
    private final String lastActiveCatalog;

    public SearchSource(SourceType type, String path, String lastActiveCatalog, SourceOwner owner) {
        this(type, path, lastActiveCatalog, owner, null);
    }

    public SearchSource(SourceType type, String path, String lastActiveCatalog, SourceOwner owner, Long id) {
        this.id = id == null ? getRandomId() : id;
        this.owner = Validate.notNull(owner);
        this.type = Validate.notNull(type);
        this.path = PathUtils.normalizePath(path);
        this.lastActiveCatalog = PathUtils.normalizePath(lastActiveCatalog);
    }

    public SearchSource(SourceType type, String path, SourceOwner owner){
        this(type, path, owner, null);
    }

    public SearchSource(SourceType type, String path, SourceOwner owner, Long id) {
        this(type, path, null, owner, id);
    }

    public SourceType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public SourceOwner getOwner() {
        return owner;
    }

    public boolean isPublic() {
        return getOwner().isDefault();
    }

    public String getLastActiveCatalog() {
        return lastActiveCatalog;
    }

    public boolean hasLastActiveCatalog() {
        return StringUtils.isNotBlank(lastActiveCatalog);
    }

    public Long getId() {
        return id;
    }

    private static Long getRandomId() {
        return Math.abs(new Random().nextLong());
    }


    @Override
    public String toString() {
        return "SearchSource{" +
                "id=" + id +
                ", type=" + type +
                ", owner=" + owner +
                ", path='" + path + '\'' +
                ", lastActiveCatalog='" + lastActiveCatalog + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchSource that = (SearchSource) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
