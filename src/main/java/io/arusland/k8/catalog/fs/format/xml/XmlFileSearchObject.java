package io.arusland.k8.catalog.fs.format.xml;

import io.arusland.k8.catalog.ObjectType;
import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.fs.format.TextFileSearchObject;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceOwner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ruslan on 29.06.2015.
 */
public class XmlFileSearchObject extends TextFileSearchObject {
    public final static String TAGS = "tags";
    public final static String ATTR_NAMES = "attr_names";
    public final static String ATTR_VALUES = "attr_values";
    private final XmlFileInfoExtractor helper = new XmlFileInfoExtractor();
    private final static String[] FIELDS;

    static {
        List<String> arr =  new LinkedList<>(Arrays.asList(TextFileSearchObject.FIELDS));
        arr.add(TAGS);
        arr.add(ATTR_NAMES);
        arr.add(ATTR_VALUES);

        FIELDS = arr.toArray(new String[arr.size()]);
    }

    public XmlFileSearchObject(File file, SearchSource source) throws IOException {
        super(file, source);
        helper.load(file);
    }

    public XmlFileSearchObject(PropertyGetter fields, SearchSource source) {
        super(fields, source);
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.FILE_XML;
    }

    @Override
    public Map<String, Object> toDoc() {
        final Map<String, Object> doc =  super.toDoc();

        doc.put(TAGS, helper.getTags());
        doc.put(ATTR_NAMES, helper.getAttributeNames());
        doc.put(ATTR_VALUES, helper.getAttributeValues());

        return doc;
    }

    @Override
    public String getContent(){
        return helper.getContent();
    }

    public static String[] getFields() {
        return FIELDS;
    }
}
