package com.cxy890.server;

import com.cxy890.server.handler.ServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;


/**
 * @author BD-PC27
 */
@Slf4j
public class Server {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        System.setProperty("io.netty.noUnsafe", "true");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture sync = bootstrap.bind(port).sync();
            log.info("Server started on port : " + port);
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Server start error, message : ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
