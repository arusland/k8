package io.arusland.k8.catalog;

import io.arusland.k8.source.SourceType;
import io.arusland.k8.util.HashUtils;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;
import org.elasticsearch.search.SearchHitField;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruslan on 27.06.2015.
 */
public class SearchObject {
    public final static String NAME = "name";
    public final static String PATH = "path";
    public final static String SIZE = "size";
    public final static String TYPE = "type";
    public final static String HASH = "hash";
    public final static String ICON  = "icon";
    public final static String IS_CATALOG = "is_catalog";
    public final static String CONTENT = "content";
    public final static String CREATE_DATE = "create_date";
    public final static String MODIFY_DATE = "modify_date";
    protected final static String[] FIELDS = Arrays.asList(NAME, PATH, SIZE, TYPE,
            HASH, ICON, IS_CATALOG, CONTENT, CREATE_DATE, MODIFY_DATE).toArray(new String[10]);

    private final String name;
    private final String path;
    private final String id;
    private final long size;
    private final String hash;
    private final String content;
    private final SourceType sourceType;
    private final ObjectType objectType;
    private final String icon;
    private final boolean isCatalog;
    private final Date createDate;
    private final Date modifyDate;

    public SearchObject(String name, String path, long size, String hash, String content,
                        SourceType sourceType, ObjectType objectType, String icon,
                        Date createDate, Date modifiedDate, boolean isCatalog) {
        this.isCatalog = isCatalog;
        this.name = Validate.notBlank(name);
        this.path = Validate.notBlank(path);
        this.id = HashUtils.sha1Hex(path.toLowerCase());
        this.size = size;
        this.hash = hash;
        this.content = content;
        this.sourceType = sourceType;
        this.objectType = objectType;
        this.icon = Validate.notBlank(icon);
        this.createDate = Validate.notNull(createDate);
        this.modifyDate = Validate.notNull(modifiedDate);
    }

    public SearchObject(PropertyGetter fields, SourceType sourceType){
        this(fields.getValue(NAME), fields.getValue(PATH), fields.getLong(SIZE),
                fields.getValue(HASH), fields.getValue(CONTENT), sourceType, fields.getObjectType(TYPE),
                fields.getValue(ICON), fields.getDate(CREATE_DATE), fields.getDate(MODIFY_DATE),
                fields.getValue(IS_CATALOG));
    }

    public String getId() {
        return id;
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

    public Date getCreateDate() {
        return createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public boolean isCatalog() {
        return isCatalog;
    }

    public String[] getFields() {
        return FIELDS;
    }

    public Map<String, Object> toDoc(){
        Map<String, Object> doc = new HashMap<>();

        doc.put(NAME, name);
        doc.put(PATH, path);
        doc.put(SIZE, size);
        doc.put(TYPE, objectType);
        doc.put(HASH, hash);
        doc.put(ICON, icon);
        doc.put(IS_CATALOG, isCatalog);
        doc.put(CONTENT, content);
        doc.put(CREATE_DATE, new Timestamp(createDate.getTime()));
        doc.put(MODIFY_DATE, new Timestamp(modifyDate.getTime()));

        return doc;
    }

    @Override
    public String toString() {
        return "SearchObject{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", hash='" + hash + '\'' +
                ", sourceType=" + sourceType +
                ", objectType=" + objectType +
                ", icon='" + icon + '\'' +
                ", isCatalog=" + isCatalog +
                ", path='" + path + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                '}';
    }
}
