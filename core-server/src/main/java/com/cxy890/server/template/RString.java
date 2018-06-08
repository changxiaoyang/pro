package com.cxy890.server.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author BD-PC27
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RString {

    private String value;

    private String charset;

    public static RString of(String v, String c) {
        RString string = new RString();
        string.charset = c;
        string.value = v;
        return string;
    }
}
