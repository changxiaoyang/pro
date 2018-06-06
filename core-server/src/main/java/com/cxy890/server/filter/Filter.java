package com.cxy890.server.filter;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author BD-PC27
 */
public interface Filter {

    boolean filter(FullHttpRequest httpRequest);

}
