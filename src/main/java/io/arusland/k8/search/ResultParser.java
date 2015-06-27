package io.arusland.k8.search;

import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.catalog.fs.FileSearchObject;
import org.elasticsearch.search.SearchHit;

/**
 * Created by ruslan on 27.06.2015.
 */
public class ResultParser {
    public static SearchObject parse(SearchHit hit) {
        PropertyGetter propGetter = new PropertyGetter(hit.getFields());
        String type = propGetter.getObjectType("type").toString();

        if (type.startsWith("FILE_")) {
            FileSearchObject obj = new FileSearchObject(propGetter);
            return obj;
        } else if (type.startsWith("DB_")) {
        }

        throw new UnsupportedOperationException("Not implemented for type " + type);
    }
}
