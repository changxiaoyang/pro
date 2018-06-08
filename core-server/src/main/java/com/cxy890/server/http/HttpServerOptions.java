package com.cxy890.server.http;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.multipart.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BD-PC27
 */
@Slf4j
class HttpServerOptions implements HttpServerRequest {

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    private String uri;

    private HttpMethod method;

    private static Map<String, Map<String, Object>> sessions = new ConcurrentHashMap<>();

    private Map<String, String> headers = new HashMap<>();

    private Map<String, Object> attributes = new HashMap<>();

    private Map<String, Object> params = new HashMap<>();

    @Override
    public String uri() {
        return uri;
    }

    public String cookie() {
        return this.headers.get("Cookie");
    }

    @Override
    public Map<String, Object> session() {
        return sessions.computeIfAbsent(cookie(), k -> new HashMap<>());
    }

    @Override
    public Map<String, String> headers() {
        return headers;
    }

    @Override
    public Object header(String header) {
        return headers.get(header.toLowerCase());
    }

    @Override
    public Map<String, Object> attributes() {
        return attributes;
    }

    @Override
    public Map<String, Object> params() {
        return params;
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    HttpServerOptions option(FullHttpRequest httpRequest) throws IOException {
        this.method = httpRequest.method();
        HttpHeaders headers = httpRequest.headers();
        List<Map.Entry<String, String>> entries = headers.entries();
        for (Map.Entry<String, String> entry : entries) {
            this.headers.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        String uri = URLDecoder.decode(httpRequest.uri(), "utf-8");
        if (HttpMethod.GET.equals(method)) {
            log.debug(uri + "  Get");
            parseGet(uri);
        }
        if (HttpMethod.POST.equals(method)) {
            log.debug(uri + "  Post");
            this.uri = uri.split("\\?")[0];
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, httpRequest);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
            for(InterfaceHttpData data:postData){
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    if (data instanceof MixedAttribute) {
                        MixedAttribute attribute = (MixedAttribute) data;
                        this.params.put(attribute.getName(), attribute.getValue());
                    }
                    if (data instanceof MemoryAttribute) {
                        MemoryAttribute attribute = (MemoryAttribute) data;
                        this.params.put(attribute.getName(), attribute.getValue());
                    }
                }
            }
        }
        return this;
    }

    private void parseGet(String uri) {
        if (uri != null && uri.contains("?")) {
            String[] split = uri.split("\\?");
            if (split.length > 1) {
                Map<String, Object> paramsMap = new HashMap<>();
                String[] params = split[1].split("&{1,2}");
                for (String str : params) {
                    String[] kv = str.split("=");
                    paramsMap.put(kv[0], kv[1]);
                }
                this.uri = split[0];
                this.params = paramsMap;
                return;
            }
        }
        this.uri = uri;
    }

}
