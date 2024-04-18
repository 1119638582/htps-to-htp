package com.hu.nettyStudy.example.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author HuNingHao
 * @version 1.0
 * @date 2024/4/8 18:06
 */


public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 自定义的Handler需要继承Netty规定好的HandlerAdapter
     * 才能被Netty框架所关联，有点类似SpringMVC的适配器模式
     **/

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //给pipeline管道设置处理器
        socketChannel.pipeline().addLast(new ServerHandler());
    }
}
