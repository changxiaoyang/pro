package com.cxy890.cxy.util;

/**
 * String 操作工具类
 *
 * Created by ChangXiaoyang on 2017/5/18.
 */
public final class StringUtil {

    /**
     * 首字母小写
     *
     * @param str 原字符串
     * @return 首字母小写的字符串
     */
    public static String firstToLower(String str) {
        if (isNull(str))
            return "";
        char[] chars = str.toCharArray();
        if (('a' <= chars[0] && chars[0] <= 'z') || (('A' <= chars[0] && chars[0] <= 'Z')))
            chars[0] = (char) (chars[0] - 'A' + 'a');
        return String.valueOf(chars);
    }

    /**
     * 首字母大写
     *
     * @param str 原字符串
     * @return 首字母大写的字符串
     */
    public static String firstToUpper(String str) {
        if (isNull(str))
            return "";
        char[] chars = str.toCharArray();
        if (('a' <= chars[0] && chars[0] <= 'z') || (('A' <= chars[0] && chars[0] <= 'Z')))
            chars[0] = (char) (chars[0] - 'a' + 'A');
        return String.valueOf(chars);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 元字符串
     * @return null?true:false
     */
    public static boolean isNull (String str) {
        return str == null || str.trim().length() == 0;
    }

}
