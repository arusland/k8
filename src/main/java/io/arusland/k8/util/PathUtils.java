package io.arusland.k8.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ruslan on 28.06.2015.
 */
public class PathUtils {
    public static String normalizePath(String path){
        if (StringUtils.isBlank(path)){
            return StringUtils.EMPTY;
        }

        path = path.trim();

        while (path.endsWith("\\") && !path.endsWith(":\\")){
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }
}
