package com.hu.nettyStudy.htps.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author HuNingHao
 * @version 1.0
 * @date 2024/4/9 16:18
 */
public class HttpServerFrontendHandler extends ChannelInboundHandlerAdapter {
    public static final Logger logger= LoggerFactory.getLogger(HttpServerFrontendHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest) msg;
            URI uri = new URI(request.uri());
            String scheme = uri.getScheme();
            if ("https".equalsIgnoreCase(scheme)) {
                // 如果请求是HTTPS，则将其转换为HTTP
                String modifiedUri = "http://" + uri.getHost() + uri.getPath();
                FullHttpRequest newRequest = new DefaultFullHttpRequest(request.protocolVersion(), request.method(), modifiedUri, request.content());
                newRequest.headers().set(request.headers());
                ctx.writeAndFlush(newRequest);
            } else {
                // 如果不是HTTPS请求，直接转发
                ctx.writeAndFlush(request.retain());
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String add = channel.remoteAddress().toString();
        logger.info("收到连接：{}",add);
        channel.read();
        super.channelActive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
