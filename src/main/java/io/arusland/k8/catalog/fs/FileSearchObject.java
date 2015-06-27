package io.arusland.k8.catalog.fs;

import io.arusland.k8.catalog.KnownObjectIcons;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SourceType;
import io.arusland.k8.util.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileSearchObject extends SearchObject {
    private static Pattern ROOT_DRIVE = Pattern.compile("^(\\w):\\\\$");
    private final File file;

    public FileSearchObject(File file) throws IOException {
        super(normalizeFileName(Validate.notNull(file)), file.getAbsolutePath(), file.isDirectory() ? 0L : file.length(),
                file.isDirectory() ? HashUtils.sha1Hex(file.getAbsolutePath()) : HashUtils.sha1Hex(file),
                StringUtils.EMPTY, SourceType.FileSystem, FileTypeHelper.getObjectType(file),
                KnownObjectIcons.BINARY_FILE, file.isDirectory());

        this.file = file;
    }

    public File getFile() {
        return file;
    }

    private static String normalizeFileName(File file){
        if (StringUtils.isBlank(file.getName())){
            Matcher mc = ROOT_DRIVE.matcher(file.getAbsolutePath());

            if (mc.find()){
                return mc.group(1).toUpperCase();
            } else {
                throw new IllegalStateException("Unsupported file: " + file);
            }
        }

        return file.getName();
    }
}
