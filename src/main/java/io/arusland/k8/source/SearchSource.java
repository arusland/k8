package io.arusland.k8.source;

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

    public SearchSource(SourceType type, String path) {
        this.type = Validate.notNull(type);
        this.path = Validate.notBlank(path);
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

    public boolean isPublic(){
        return owners.isEmpty();
    }
}
