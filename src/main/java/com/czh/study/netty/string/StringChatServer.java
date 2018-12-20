package com.czh.study.netty.string;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: cai.zhenghao
 * @Description: 简单的收发消息demo，参考了github上https://github.com/netty/netty 的例子
 * @Date: Created in 2018/12/17  5:49 PM
 * @Modified By:
 */
public class StringChatServer {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8023"));
    static Map<String, Channel> chMap = new ConcurrentHashMap<String, Channel>();

    public static void main(String[] args) throws Exception {
        //configure ssl
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new StringServerInitializer(sslCtx));

            // Bind and start to accept incoming connections.
            ChannelFuture channelFuture = b.bind(PORT).sync();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String line = null;
                try {
                    line = in.readLine();
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }

                line = line + "\r\n";
                // Sends the received line to the server.have to end by "\r\n"
                Iterator<Map.Entry<String,Channel>> iterator = chMap.entrySet().iterator();
                while (iterator.hasNext()){
                    iterator.next().getValue().writeAndFlush(line);
                }
            }

//            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
