package com.cxy890.cxy.server.init;

import com.cxy890.cxy.server.handler.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * NettyServerInitializer
 *
 * Created by ChangXiaoyang on 2017/8/13.
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
         ChannelPipeline pipeline = socketChannel.pipeline();
         // 以("\n")为结尾分割的 解码器
//         pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
         // 字符串解码 和 编码
         pipeline.addLast(new HttpRequestDecoder());
         pipeline.addLast(new HttpResponseEncoder());

         // 自己的逻辑Handler
         pipeline.addLast(new NettyServerHandler());
    }
}
