package com.hu.nettyStudy.urlProxy;

import com.hu.nettyStudy.config.PropertiesConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

/**
 * @author HuNingHao
 * @version 1.0
 * @date 2024/4/15 14:43
 */
public class ProxyHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String host = request.headers().get(HttpHeaderNames.HOST);
            if ("www.example.com".equalsIgnoreCase(host)) {
                // 如果请求的是指定的网站，重定向到本地的Netty服务
                redirectRequest(request, ctx);
            } else {
                // 否则，转发原始请求到远程服务器
                forwardRequest(request, ctx);
            }
        }
    }


    private static void redirectRequest(HttpRequest request, ChannelHandlerContext ctx) {
        // 构造重定向响应
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, "http://localhost:8888");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);

        // 发送重定向响应给客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    private static void forwardRequest(HttpRequest request, ChannelHandlerContext ctx) {
        int port = Integer.parseInt(PropertiesConfig.getProperty("server.port", "8888"));

        // 连接远程服务器
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new HttpClientCodec());
                        p.addLast(new SimpleChannelInboundHandler<HttpObject>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
                                // 将远程服务器的响应转发回客户端
                                ctx.writeAndFlush(msg);
                            }
                        });
                    }
                });
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", port));
        future.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                futureListener.channel().writeAndFlush(request);
            } else {
                futureListener.channel().close();
            }
        });
    }
}
