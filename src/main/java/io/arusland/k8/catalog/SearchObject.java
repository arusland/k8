package io.arusland.k8.catalog;

import io.arusland.k8.source.SourceType;
import org.apache.commons.lang3.Validate;

/**
 * Created by ruslan on 27.06.2015.
 */
public class SearchObject {
    final private String name;
    final private String path;
    final private long size;
    final private String hash;
    final private String content;
    final private SourceType sourceType;
    final private ObjectType objectType;
    final private String icon;
    final private boolean isCatalog;

    public SearchObject(String name, String path, long size, String hash, String content,
                        SourceType sourceType, ObjectType objectType, String icon, boolean isCatalog) {
        this.isCatalog = isCatalog;
        this.name = Validate.notBlank(name);
        this.path = Validate.notBlank(path);
        this.size = size;
        this.hash = hash;
        this.content = content;
        this.sourceType = sourceType;
        this.objectType = objectType;
        this.icon = Validate.notBlank(icon);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public String getHash() {
        return hash;
    }

    public String getContent() {
        return content;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isCatalog() {
        return isCatalog;
    }

    @Override
    public String toString() {
        return "SearchObject{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", sourceType=" + sourceType +
                ", objectType=" + objectType +
                ", hash='" + hash + '\'' +
                ", icon='" + icon + '\'' +
                ", path='" + path + '\'' +
                ", isCatalog=" + isCatalog +
                '}';
    }
}
