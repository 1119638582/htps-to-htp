package com.hu.nettyStudy.urlProxy;
import com.hu.nettyStudy.config.PropertiesConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
/**
 * @author HuNingHao
 * @version 1.0
 * @date 2024/4/15 14:35
 */
public class ProxyServer {

    private final static Logger logger = LoggerFactory.getLogger(ProxyServer.class);

    private static final String REMOTE_HOST = "www.example.com";
    private static final int REMOTE_PORT = 80;
    private static final int LOCAL_PORT = 8888;

    public static void main(String[] args) throws InterruptedException {
        int port = Integer.parseInt(PropertiesConfig.getProperty("server.port", "8888"));
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ProxyInitializer());
            ChannelFuture f = b.bind(80).sync();
            logger.info("代理服务启动成功！代理端口: {}",443);
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
