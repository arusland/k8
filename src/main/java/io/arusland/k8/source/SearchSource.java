package io.arusland.k8.source;

import io.arusland.k8.util.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Created by ruslan on 27.06.2015.
 */
public class SearchSource {
    private final SourceType type;
    private final String path;
    private final SourceOwner owner;
    private final String lastActiveCatalog;

    public SearchSource(SourceType type, String path, String lastActiveCatalog, SourceOwner owner) {
        this.owner = Validate.notNull(owner);
        this.type = Validate.notNull(type);
        this.path = PathUtils.normalizePath(Validate.notBlank(path));
        this.lastActiveCatalog = PathUtils.normalizePath(lastActiveCatalog);
    }

    public SearchSource(SourceType type, String path, SourceOwner owner) {
        this(type, path, null, owner);
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

    @Override
    public String toString() {
        return "SearchSource{" +
                "type=" + type +
                ", path='" + path + '\'' +
                ", owner=" + owner +
                ", lastActiveCatalog='" + lastActiveCatalog + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchSource that = (SearchSource) o;

        if (lastActiveCatalog != null ? !lastActiveCatalog.equals(that.lastActiveCatalog) : that.lastActiveCatalog != null)
            return false;
        if (!owner.equals(that.owner)) return false;
        if (!path.equals(that.path)) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + (lastActiveCatalog != null ? lastActiveCatalog.hashCode() : 0);
        return result;
    }
}
