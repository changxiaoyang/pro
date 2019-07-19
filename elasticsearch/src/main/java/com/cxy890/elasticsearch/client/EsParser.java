package com.cxy890.elasticsearch.client;

import com.cxy890.config.util.Strings;
import com.cxy890.elasticsearch.annotations.EsFiled;
import com.cxy890.elasticsearch.annotations.EsId;
import com.cxy890.elasticsearch.annotations.EsIndex;
import com.cxy890.elasticsearch.enums.EField;

import java.lang.reflect.Field;

/**
 * @author BD-PC27
 */
public class EsParser {

    public static EsIndex getIndex(Class<?> aClass) {
        if (aClass.isAnnotationPresent(EsIndex.class)) {
            return aClass.getAnnotation(EsIndex.class);
        }
        return null;
    }

    public static <T> String getId(T entity, Class<T> aClass)  {
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(EsId.class)) {
                try {
                    field.setAccessible(true);
                    return field.get(entity).toString();
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return null;
    }

    public static String getSource(Class<?> aClass) {
        StringBuilder source = new StringBuilder("{\"properties\":{");
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(EsFiled.class)) {
                EsFiled esFiled = field.getAnnotation(EsFiled.class);
                if (esFiled.ignore()) continue;
                source.append("\"").append(esFiled.value()).append("\":{\"type\":\"").append(esFiled.type().value()).append("\"");
                if (!Strings.isNull(esFiled.analyzer())) {
                    source.append(",\"analyzer\":\"").append(esFiled.analyzer()).append("\"");
                }
                if (EField.DATE.equals(esFiled.type())) {
                    if (Strings.isNull(esFiled.format()))
                        source.append(",\"format\":\"strict_date_optional_time||epoch_millis\"");
                    else
                        source.append(",\"format\":\"").append(esFiled.format()).append("\"");
                }
                source.append("},");
            }
        }
        source.deleteCharAt(source.length() - 1).append("}}");
        return source.toString();
    }

}
