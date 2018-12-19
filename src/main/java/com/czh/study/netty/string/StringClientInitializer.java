package com.czh.study.netty.string;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.ssl.SslContext;


/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2018/12/19  5:02 PM
 * @Modified By:
 */
public class StringClientInitializer extends ChannelInitializer<SocketChannel>{

    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringDecoder ENCODER = new StringDecoder();

    private static final StringClientHandler CLIENT_HANDLER = new StringClientHandler();

    private final SslContext sslCtx;

    public StringClientInitializer(SslContext sslCtx){
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        if(sslCtx != null){
            pipeline.addLast(sslCtx.newHandler(socketChannel.alloc(),StringChatClient.HOST,StringChatClient.PORT));
        }

        // Add the text line codec combination first,事实上是通过换行符来分割多个string避免粘包
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // the encoder and decoder are static as these are sharable
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);

        // and then business logic.
        pipeline.addLast(CLIENT_HANDLER);

    }
}
