package com.czh.study.netty.protocolbuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
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
 * 参考 https://marlay.iteye.com/blog/2427985
 */
public class ProtoChatServer {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
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
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(socketChannel.alloc()));
                            }

                            p.addLast(new CustomProtobufDecoder());
                            p.addLast(new CustomProtobufEncoder());
                            p.addLast(new ProtoChatServerHandler());
                        }
                    });

            // Bind and start to accept incoming connections.
            ChannelFuture channelFuture = b.bind(PORT).sync();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = null;
                try {
                    msg = in.readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                // Sends the received line to the server.
                Object msgOb = null;
                if(msg.equals("stu")){
                    StudentPb.Student student =  StudentPb.Student.newBuilder()
                            .setId(100).setName("学生klc")
                            .setEmail("student@163.com").build();
                    msgOb = student;
                }else if(msg.equals("tea")){
                    TeacherPb.Teacher teacher =  TeacherPb.Teacher.newBuilder()
                            .setId(100).setName("老师")
                            .setEmail("teacher@163.com")
                            .setNick("Hehe")
                            .build();
                    msgOb = teacher;
                }else {
                    System.out.println("消息类型不支持");
                    continue;
                }
                Iterator<Map.Entry<String, Channel>> iterator = chMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    iterator.next().getValue().writeAndFlush(msgOb);
                }
            }

//            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
