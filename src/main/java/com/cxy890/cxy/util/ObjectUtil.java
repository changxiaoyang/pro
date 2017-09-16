package com.cxy890.cxy.util;

import java.util.Collection;
import java.util.Map;

/**
 * 对象管理工具类
 *
 * Created by ChangXiaoyang on 2017/5/18.
 */
public final class ObjectUtil {

    /**
     * 深度判空
     *
     * @param obj 对象
     * @return true
     */
    @SuppressWarnings("unchecked")
    public static boolean isDeepEmpty(Object obj) {
        if (obj == null)
            return true;
        if (obj instanceof CharSequence)
            return obj.toString().trim().length() == 0;
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty()
                    || ((Collection) obj).stream().allMatch(ObjectUtil::isDeepEmpty);
        }
        if (obj instanceof Map)
            return ((Map) obj).isEmpty();
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (Object anObject : object) {
                if (!isDeepEmpty(anObject)) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
}
