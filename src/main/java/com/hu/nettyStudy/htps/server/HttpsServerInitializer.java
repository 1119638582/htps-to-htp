package com.hu.nettyStudy.htps.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import io.netty.channel.socket.SocketChannel;

/**
 *
 * @author HuNingHao
 * @version 1.0
 * @date 2024/4/9 15:39
 */
public class HttpsServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
//        p.addLast(sslCtx.newHandler(ch.alloc()));
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(new HttpServerFrontendHandler());
    }
}
