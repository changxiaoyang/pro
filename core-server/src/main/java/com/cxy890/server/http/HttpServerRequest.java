package com.cxy890.server.http;

import io.netty.handler.codec.http.HttpMethod;

import java.util.Map;

/**
 * @author BD-PC27
 */
public interface HttpServerRequest {

    String uri();

    Map<String, Object> session();

    Map<String, String> headers();

    Object header(String header);

    Map<String, Object> attributes();

    Map<String, Object> params();

    HttpMethod method();

}
