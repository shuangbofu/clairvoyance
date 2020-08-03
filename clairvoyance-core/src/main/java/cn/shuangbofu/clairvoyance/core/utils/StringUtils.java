package cn.shuangbofu.clairvoyance.core.utils;

/**
 * Created by shuangbofu on 2020-05-22 16:00
 *
 * @author shuangbofu
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() != 0;
    }

    public static String emptyGet(String str, String defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        } else {
            return str;
        }
    }
}
