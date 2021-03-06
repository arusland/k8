package io.arusland.k8.catalog.fs;

import io.arusland.k8.catalog.ObjectType;

import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Created by ruslan on 27.06.2015.
 */
public class FileTypeHelper {
    @NotNull
    public static ObjectType getObjectType(File file){
        if (file.isFile() && file.getName().toLowerCase().endsWith(".xml")){
            return ObjectType.FILE_XML;
        }

        return ObjectType.FILE_BINARY;
    }
}
