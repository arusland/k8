package io.arusland.k8.catalog.fs;

import io.arusland.k8.catalog.KnownObjectIcons;
import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import io.arusland.k8.util.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileSearchObject extends SearchObject {
    private static final String OBJECT_FILE = "file";
    private final static Pattern ROOT_DRIVE = Pattern.compile("^(\\w):\\\\$");
    private final File file;

    public FileSearchObject(File file, SearchSource source) throws IOException {
        super(normalizeFileName(Validate.notNull(file)), file.getAbsolutePath(), file.isDirectory() ? 0L : file.length(),
                file.isDirectory() ? HashUtils.sha1Hex(file.getAbsolutePath().toLowerCase()) : HashUtils.sha1Hex(file),
                StringUtils.EMPTY, SourceType.FileSystem, FileTypeHelper.getObjectType(file),
                KnownObjectIcons.BINARY_FILE, getFileCreateTime(file), new Date(file.lastModified()), file.isDirectory(), source);

        this.file = file;
    }

    public FileSearchObject(PropertyGetter fields, SearchSource source){
        super(fields, SourceType.FileSystem, source);
        this.file = null;
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

    @Override
    public Map<String, Object> toDoc() {
        return super.toDoc();
    }

    @Override
    public String getSearchType() {
        return OBJECT_FILE;
    }

    private static Date getFileCreateTime(File file) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

        return new Date(attrs.creationTime().toMillis());
    }
}
