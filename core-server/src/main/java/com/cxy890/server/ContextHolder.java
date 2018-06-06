package com.cxy890.server;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author BD-PC27
 */
public class ContextHolder {

    private static ThreadLocal<FullHttpRequest> request = new InheritableThreadLocal<>();

    public static void setRequest(FullHttpRequest request) {
        ContextHolder.request.set(request);
    }

    public static FullHttpRequest currentRequest() {
        return request.get();
    }

}
