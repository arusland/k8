package io.arusland.k8.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ruslan on 27.06.2015.
 */
public class HashUtils {
    public static String sha1Hex(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            return DigestUtils.sha1Hex(stream);
        }
    }

    public static String sha1Hex(InputStream stream) throws IOException {
        return DigestUtils.sha1Hex(stream);
    }

    public static String sha1Hex(String data){
        return DigestUtils.sha1Hex(data);
    }
}
