package io.arusland.k8.catalog.fs;

import java.io.File;
import java.util.function.Predicate;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileSkipProvider implements Predicate<File>  {
    @Override
    public boolean test(final File file) {
        // TODO: read from config file!
        return "pagefile.sys".equals(file.getName())
                || "hiberfil.sys".equals(file.getName());
    }
}
