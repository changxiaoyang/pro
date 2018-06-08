package com.cxy890.server.handler;

import com.cxy890.server.template.RString;
import com.cxy890.server.util.ByteUtil;
import com.cxy890.server.http.HttpExchanger;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * ServerHandler
 *
 * Created by ChangXiaoyang on 2017/8/13.
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FullHttpRequest)) return;

        FullHttpRequest httpRequest = (FullHttpRequest) msg;
        String path = httpRequest.uri();
        if ("/favicon.ico".equals(path)) {
            returnIco(ctx);
            return;
        }

        RString result = HttpExchanger.invokeAsString(httpRequest);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.wrappedBuffer(result.getValue().getBytes(result.getCharset())));
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_ENCODING, result.getCharset());
        response.headers().set(CONTENT_LENGTH,
                response.content().readableBytes());
        response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    private void returnIco(ChannelHandlerContext ctx) throws IOException {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.wrappedBuffer(ByteUtil.getIcon()));
        response.headers().set(CONTENT_TYPE, "image/x-icon");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set("Connection", HttpHeaderValues.KEEP_ALIVE);
        ctx.writeAndFlush(response);
    }



}
