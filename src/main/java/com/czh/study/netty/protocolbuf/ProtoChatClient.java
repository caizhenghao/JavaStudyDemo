package com.czh.study.netty.protocolbuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Author: cai.zhenghao
 * @Description: 简单的收发消息demo，参考了github上https://github.com/netty/netty 的例子
 * @Date: Created in 2018/12/17  4:36 PM
 * @Modified By:
 * 参考 https://marlay.iteye.com/blog/2427985
 */
public class ProtoChatClient {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
//    static final int SIZE = Integer.parseInt(System.getProperty("size","256"));

    public static void main(String[] args) throws Exception {
        //config ssl
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(socketChannel.alloc(), HOST, PORT));
                            }
                            p.addLast(new CustomProtobufDecoder());
                            p.addLast(new CustomProtobufEncoder());
                            p.addLast(new ProtoChatClientHandler());
                        }
                    });

            // Start the connection attempt.
            ChannelFuture lastWriteFuture = null;
            ChannelFuture channelFuture = b.connect(HOST, PORT).sync();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                String msg = in.readLine();
                if (msg == null || "quit".equalsIgnoreCase(msg)) {
                    break;
                }

                // Sends the received line to the server.
                Object msgOb = null;
                if (msg.equals("stu")) {
                    StudentPb.Student student = StudentPb.Student.newBuilder()
                            .setId(100).setName("学生klc")
                            .setEmail("student@qq.com").build();
                    msgOb = student;
                } else if (msg.equals("tea")) {
                    TeacherPb.Teacher teacher = TeacherPb.Teacher.newBuilder()
                            .setId(100).setName("老师")
                            .setNick("Hehe")
                            .setEmail("teacher@qq.com").build();
                    msgOb = teacher;
                } else {
                    System.out.println("消息类型不支持");
                    continue;
                }

                if (channelFuture.channel() != null) {
                    lastWriteFuture = channelFuture.channel().writeAndFlush(msgOb);
                }
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.awaitUninterruptibly();
            }
            //wait for channel close
//            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }

}
