package com.cxy890.server.http;

import com.cxy890.server.filter.FilterRegister;
import com.cxy890.server.filter.PathRegister;
import com.cxy890.server.template.RString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author BD-PC27
 */
@Slf4j
public class HttpExchanger {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public static RString invokeAsString(FullHttpRequest httpRequest) throws Exception {
        //todo: 封装个HttpServerRequest, HttpServerResponse,  实现下 Filter Handler支持

        HttpServerRequest request = new HttpServerOptions().option(httpRequest);
        ContextHolder.setRequest(request);

        Object agent = request.header("User-Agent");
        String charset = agent.toString().startsWith("Mozilla") ? "GBK" : "utf-8";
        Object encoding = request.header("Return-Encoding");
        if (encoding != null) charset = encoding.toString();
        if (FilterRegister.deny(httpRequest)) return RString.of("{}", charset);
        String uri = request.uri();
        Map<String, Object> parameter = request.params();

        Method method = PathRegister.getMethod(uri);
        if (method == null)
            throw new NoSuchMethodException("-------------");
        Object instance = PathRegister.getInstance(uri);
        int count = method.getParameterCount();
        if (method.getParameterCount() == 0) {
            return RString.of(toJson(method.invoke(instance)), charset);
        }
        Object[] params = new Object[count];
        List<String> stringList = PathRegister.getParameterMap(uri);
        for (int i = 0; i < count; i++) {
            params[i] = parameter.get(stringList.get(i));
        }
        return RString.of(toJson(method.invoke(instance, params)), charset);
    }

}
