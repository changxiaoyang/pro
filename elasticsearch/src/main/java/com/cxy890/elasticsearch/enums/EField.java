package com.cxy890.elasticsearch.enums;

/**
 * @author BD-PC27
 */
public enum EField {

    TEXT, KEYWORD, DATE, LONG, DOUBLE, BOOLEAN, IP,
    OBJECT, NESTED, GEO_POINT, GEO_SHAPE, COMPLETION;

    public String value() {
        return name().toLowerCase();
    }

}
