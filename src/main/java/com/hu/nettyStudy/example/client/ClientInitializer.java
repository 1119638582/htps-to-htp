package com.hu.nettyStudy.example.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author HuNingHao
 * @version 1.0
 * @date 2024/4/8 18:34
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //添加客户端通道的处理器
        ch.pipeline().addLast(new ClientHandler());
    }
}
