package com.cxy890.server.http;

/**
 * @author BD-PC27
 */
public class ContextHolder {

    private static ThreadLocal<HttpServerRequest> request = new InheritableThreadLocal<>();

    static void setRequest(HttpServerRequest request) {
        ContextHolder.request.set(request);
    }

    public static HttpServerRequest currentRequest() {
        return request.get();
    }

}
