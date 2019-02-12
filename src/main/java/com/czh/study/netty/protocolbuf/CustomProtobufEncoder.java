package com.czh.study.netty.protocolbuf;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2019/1/31  6:26 PM
 * @Modified By:
 * 参考 https://marlay.iteye.com/blog/2427985
 */
public class CustomProtobufEncoder extends MessageToByteEncoder<MessageLite> {

    @Override
    protected void encode(
            ChannelHandlerContext ctx, MessageLite msg, ByteBuf out) throws Exception {


        byte[] body = msg.toByteArray();
        byte[] header = encodeHeader(msg, (short)body.length);

        out.writeBytes(header);
        out.writeBytes(body);

        return;
    }

    private byte[] encodeHeader(MessageLite msg, short bodyLength) {
        byte messageType = 0x0f;

        if (msg instanceof StudentPb.Student) {
            messageType = 0x00;
        } else if (msg instanceof TeacherPb.Teacher) {
            messageType = 0x01;
        }

        byte[] header = new byte[4];
        header[0] = (byte) (bodyLength & 0xff);
        header[1] = (byte) ((bodyLength >> 8) & 0xff);
        header[2] = 0; // 保留字段
        header[3] = messageType;

        return header;

    }
}
