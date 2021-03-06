package io.arusland.k8.catalog.fs.format;

import io.arusland.k8.catalog.ObjectType;
import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.fs.FileSearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceOwner;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by ruslan on 28.06.2015.
 */
public class TextFileSearchObject extends FileSearchObject {
    private String content;

    public TextFileSearchObject(File file, SearchSource source) throws IOException {
        super(file, source);
    }

    public TextFileSearchObject(PropertyGetter fields, SearchSource source) {
        super(fields, source);
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.FILE_TEXT;
    }

    @Override
    public String getContent() {
        if (content == null){
            try {
                content = FileUtils.readFileToString(getFile());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        return content;
    }
}
