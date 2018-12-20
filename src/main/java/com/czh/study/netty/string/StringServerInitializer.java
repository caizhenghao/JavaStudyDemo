package com.czh.study.netty.string;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;


/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2018/12/19  5:02 PM
 * @Modified By:
 */
public class StringServerInitializer extends ChannelInitializer<SocketChannel>{

    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();

    private static final StringServerHandler SERVER_HANDLER = new StringServerHandler();

    private final SslContext sslCtx;

    public StringServerInitializer(SslContext sslCtx){
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        if(sslCtx != null){
            pipeline.addLast(sslCtx.newHandler(socketChannel.alloc()));
        }

        // Add the text line codec combination first,事实上是通过换行符来分割多个string避免粘包
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // the encoder and decoder are static as these are sharable
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);

        // and then business logic.
        pipeline.addLast(SERVER_HANDLER);

    }
}
