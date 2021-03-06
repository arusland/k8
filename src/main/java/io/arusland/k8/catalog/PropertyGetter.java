package io.arusland.k8.catalog;

import io.arusland.k8.catalog.fs.FileSearchObject;
import io.arusland.k8.catalog.fs.format.TextFileSearchObject;
import io.arusland.k8.catalog.fs.format.xml.XmlFileSearchObject;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;
import org.elasticsearch.search.SearchHitField;

import java.util.*;

/**
 * Created by ruslan on 27.06.2015.
 */
public class PropertyGetter {
    public static final String[] ALL_FIELDS;

    private final static DateTimeFormatter ISO_FORMAT = ISODateTimeFormat.dateTime();
    private final Map<String, SearchHitField> fields;

    public PropertyGetter(Map<String, SearchHitField> fields) {
        this.fields = Validate.notNull(fields);
    }

    public <V> V getValue(String name) {
        return fields.get(name).getValue();
    }

    public Date getDate(String name) {
        DateTime time = ISO_FORMAT.parseDateTime(getValue(name));

        return time.toDate();
    }

    public Long getLong(String name) {
        Integer val = getValue(name);

        return Long.valueOf(val);
    }

    public ObjectType getObjectType() {
        return ObjectType.valueOf(getValue(SearchObject.TYPE));
    }

    public String getIndex(){
        throw new IllegalStateException("Not implemented!");
    }


    static {
        Set<String> collect = new HashSet<>();

        collect.addAll(Arrays.asList(SearchObject.getFields()));
        collect.addAll(Arrays.asList(FileSearchObject.getFields()));
        collect.addAll(Arrays.asList(TextFileSearchObject.getFields()));
        collect.addAll(Arrays.asList(XmlFileSearchObject.getFields()));

        ALL_FIELDS = collect.toArray(new String[collect.size()]);
    }
}
