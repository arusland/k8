package io.arusland.k8.catalog;

import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceType;
import io.arusland.k8.util.HashUtils;
import org.apache.commons.lang3.Validate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruslan on 27.06.2015.
 */
public abstract class SearchObject {
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
    private final SearchSource source;

    public SearchObject(String name, String path, long size, String hash, String content,
                        SourceType sourceType, ObjectType objectType, String icon,
                        Date createDate, Date modifiedDate, boolean isCatalog, SearchSource source) {
        this.isCatalog = isCatalog;
        this.source = Validate.notNull(source);
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

    public SearchObject(PropertyGetter fields, SourceType sourceType, SearchSource source){
        this(fields.getValue(NAME), fields.getValue(PATH), fields.getLong(SIZE),
                fields.getValue(HASH), fields.getValue(CONTENT), sourceType, fields.getObjectType(),
                fields.getValue(ICON), fields.getDate(CREATE_DATE), fields.getDate(MODIFY_DATE),
                fields.getValue(IS_CATALOG), source);
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

    public static String[] getFields() {
        return FIELDS;
    }

    public SearchSource getSource() {
        return source;
    }

    public Map<String, Object> toDoc(){
        Map<String, Object> doc = new HashMap<>();

        doc.put(NAME, getName());
        doc.put(PATH, getPath());
        doc.put(SIZE, getSize());
        doc.put(TYPE, getObjectType());
        doc.put(HASH, getHash());
        doc.put(ICON, getIcon());
        doc.put(IS_CATALOG, isCatalog());
        doc.put(CONTENT, getContent());
        doc.put(CREATE_DATE, new Timestamp(getCreateDate().getTime()));
        doc.put(MODIFY_DATE, new Timestamp(getModifyDate().getTime()));

        return doc;
    }

    public boolean isPathEquals(SearchSource source){
        if (source == null){
            return false;
        }

        return source.getPath().toLowerCase().equals(getPath().toLowerCase());
    }

    public abstract String getSearchType();

    public String getSearchIndex() {
        return source.getIndexName();
    }

    @Override
    public String toString() {
        return "SearchObject{" +
                "name='" + getName() + '\'' +
                ", size=" + getSize() +
                ", hash='" + getHash() + '\'' +
                ", sourceType=" + getSourceType() +
                ", objectType=" + getObjectType() +
                ", icon='" + getIcon() + '\'' +
                ", isCatalog=" + isCatalog() +
                ", path='" + getPath() + '\'' +
                ", createDate=" + getCreateDate() +
                ", modifyDate=" + getModifyDate() +
                '}';
    }
}
