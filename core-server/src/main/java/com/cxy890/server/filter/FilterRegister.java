package com.cxy890.server.filter;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * todo :
 *
 * @author BD-PC27
 */
public class FilterRegister {

    private static List<Filter> filters = new ArrayList<>();

    public static void register(Filter filter) {
        filters.add(filter);
    }

    public static boolean deny(FullHttpRequest httpRequest) {
        for (Filter filter : filters) {
            if (filter.filter(httpRequest))
                return true;
        }
        return false;
    }

    public static List<Filter> get() {
        return filters;
    }
}
