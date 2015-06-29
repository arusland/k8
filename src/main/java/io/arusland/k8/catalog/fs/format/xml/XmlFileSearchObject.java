package io.arusland.k8.catalog.fs.format.xml;

import io.arusland.k8.catalog.ObjectType;
import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.fs.format.TextFileSearchObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by ruslan on 29.06.2015.
 */
public class XmlFileSearchObject extends TextFileSearchObject {
    public XmlFileSearchObject(File file) throws IOException {
        super(file);
    }

    public XmlFileSearchObject(PropertyGetter fields) {
        super(fields);
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.FILE_XML;
    }
}
