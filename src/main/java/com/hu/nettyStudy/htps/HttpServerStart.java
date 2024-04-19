package com.hu.nettyStudy.htps;

import com.hu.nettyStudy.htps.server.HttpsServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author HuNingHao
 * @version 1.0
 * @date 2024/4/9 16:43
 */
public class HttpServerStart {
    public static final Logger logger = LoggerFactory.getLogger(HttpServerStart.class);

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new HttpsServerInitializer());
            logger.info("程序启动，监听端口：{}",80);
            System.out.println("程序起床...");
            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(80);
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }  finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
