package com.cxy890.cxy.server;

import com.cxy890.cxy.annotation.AutoAssign;
import com.cxy890.cxy.annotation.AutoScan;
import com.cxy890.cxy.env.EnvContext;
import com.cxy890.cxy.server.init.NettyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty
 *
 * Created by ChangXiaoyang on 2017/8/13.
 */
@AutoScan
public class NettyServer implements IServer{

    private static EnvContext context;

    @AutoAssign
    public void setContext(EnvContext context) {
        NettyServer.context = context;
    }

    public NettyServer() {
    }

    public NettyServer(int port) {
        this.port = port;
    }

    private int port = 8080;

    @Override
    public void run() {
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new NettyServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 服务器绑定端口监听
            ChannelFuture f = b.bind(context.getAsInt("server.port", 8080)).sync();
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();

            // 可以简写为
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
