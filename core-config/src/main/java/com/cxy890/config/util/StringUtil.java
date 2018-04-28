package com.cxy890.config.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * String 操作工具类
 *
 * Created by ChangXiaoyang on 2017/5/18.
 */
public final class StringUtil {

    private static ObjectMapper mapper = new ObjectMapper();

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

    public static String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "{}";
        }
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

    /**
     * 字符串编码
     */
    public static String encode(String source) {
        if (isNull(source))
            return source;
        StringBuilder result = new StringBuilder();
        char[] sourceArray = source.toCharArray();
        for (char ch : sourceArray) {
            result.append(fillString(base62String(ch)));
        }
        return result.toString();
    }

    /**
     * 字符串解码
     */
    public static String decode(String code) {
        if (isNull(code))
            return code;
        char[] codeChar = code.toCharArray();
        int[] baseInt = new int[3];
        StringBuilder source = new StringBuilder();
        for (int i = 0; i < codeChar.length; i++) {
            int index = -1;
            //noinspection StatementWithEmptyBody
            while (++ index < 62 && BASE_CHAR[index] != codeChar[i]) ;
            if (index == 62)
                return null;
            baseInt[i % 3] = index;
            if (i % 3 == 2) {
                source.append((char) (baseInt[0] * 62 * 62 + baseInt[1] * 62 + baseInt[2]));
            }
        }
        return source.toString();
    }

    /**
     * 将char的int值转成62进制
     *
     * @param charAt 当前字符的int值
     * @return String
     */
    private static String base62String(int charAt) {
        String str;
        if (charAt == 0) {
            return "";
        } else {
            str = base62String(charAt / 62);
            return str + BASE_CHAR[charAt % 62];
        }
    }

    /**
     * 将不足3位的补0
     *
     * @param base62Str 62进制char值
     * @return 3位String
     */
    private static String fillString(String base62Str) {
        StringBuilder fillStr = new StringBuilder();
        for (int i = base62Str.length(); i < 3; i++)
            fillStr.append(BASE_CHAR[0]);
        return fillStr + base62Str;
    }

    /**
     * 顺序错乱的62个基本字符
     */
    private static final char[] BASE_CHAR = {
            'd', '4', 'g', 'h', 'I', 'N', 'Q', '0', 'c', 'V', 'Z', 'i', '5', '8', '9', 'e', '2',
            '7', 'a', 'f', 'j', 'k', 'A', 'E', 'F', 'G', 'P', 'U', 'l', 'J', 'M', 'H', 'R', 'S',
            'T', 'K', 'W', 'X', 'Y', 'L', 'O', 'o', 'r', 'm', 'w', 'x', 'y', 'p', 'B', 'C', 'D',
            'q', 't', 'u', '3', '6', '1', 'b', 'z', 'n', 's', 'v'
    };


}
