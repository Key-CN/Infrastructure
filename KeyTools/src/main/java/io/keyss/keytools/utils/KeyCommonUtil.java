package io.keyss.keytools.utils;

import java.util.Collection;

/**
 * @author Key
 * Time: 2018/6/27 11:59
 * Description:
 */
public class KeyCommonUtil {
    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return {@code true}: 空
     */
    public static boolean isCollectionEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return {@code true}: 空
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0 || "null".equals(str);
    }
}
