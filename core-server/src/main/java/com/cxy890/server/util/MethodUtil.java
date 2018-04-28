package com.cxy890.server.util;

import com.cxy890.server.filter.PathRegister;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.multipart.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BD-PC27
 */
@Slf4j
public class MethodUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    public static String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public static String invokeAsString(FullHttpRequest httpRequest) {
        try {
            HttpMethod httpMethod = httpRequest.method();

            String uri = URLDecoder.decode(httpRequest.uri(), "utf-8");
            Map<String, Object> parameter = new HashMap<>();
            if (httpMethod.equals(HttpMethod.GET)) {
                log.debug(uri + "  Get");
                Helper helper = parseGet(uri);
                uri = helper.uri;
                parameter = helper.params;
            }

            if (httpMethod.equals(HttpMethod.POST)) {
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
                        factory, httpRequest);
                List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
                for(InterfaceHttpData data:postData){
                    if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                        MemoryAttribute attribute = (MemoryAttribute) data;
                        parameter.put(attribute.getName(), attribute.getValue());
                    }
                }
            }

            Method method = PathRegister.getMethod(uri);
            Object instance = PathRegister.getInstance(uri);
            int count = method.getParameterCount();
            if (method.getParameterCount() == 0) {
                return toJson(method.invoke(instance));
            }
            Object[] params = new Object[count];
            List<String> stringList = PathRegister.getParameterMap(uri);
            for (int i = 0; i < count; i++) {
                params[i] = parameter.get(stringList.get(i));
            }
            return toJson(method.invoke(instance, params));
        } catch (Exception e) {
            return "{}";
        }

    }

    private static Helper parseGet(String uri) {
        if (uri != null && uri.contains("?")) {
            String[] split = uri.split("\\?");
            if (split.length > 1) {
                Map<String, Object> paramsMap = new HashMap<>();
                String[] params = split[1].split("&{1,2}");
                for (String str : params) {
                    String[] kv = str.split("=");
                    paramsMap.put(kv[0], kv[1]);
                }
                return new Helper(split[0], paramsMap);
            }
        }
        return new Helper(uri, Collections.emptyMap());
    }

    static class Helper{
        String uri;
        Map<String, Object> params;
        Helper(String url, Map<String, Object> params){
            this.uri = url;
            this.params = params;
        }
    }

}
