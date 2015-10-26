package io.arusland.k8.util;

/**
 * Created by ruslan on 26.10.2015.
 */
public final class ThreadUtil {
    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
