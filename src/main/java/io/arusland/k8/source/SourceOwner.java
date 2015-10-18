package io.arusland.k8.source;

import io.arusland.k8.catalog.SearchObject;
import org.apache.commons.lang3.Validate;

/**
 * Created by ruslan on 19.10.2015.
 */
public class SourceOwner {
    public static final SourceOwner DEFAULT = new SourceOwner("default");
    public static final SourceOwner NONE = new SourceOwner("none");

    private final String name;

    public SourceOwner(String name) {
        Validate.notBlank(name, "name");

        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceOwner that = (SourceOwner) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean isDefault(){
        return this.equals(DEFAULT);
    }

    @Override
    public String toString() {
        return name;
    }
}
