package com.czh.study.netty.protocolbuf;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2019/1/31  6:22 PM
 * @Modified By:
 * 参考 https://marlay.iteye.com/blog/2427985
 */
public class CustomProtobufDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() > 4) {//如果刻度长度小于包头长度，退出
            in.markReaderIndex();

            //获取包头中的body长度
            byte low = in.readByte();
            byte high = in.readByte();
            short s0 = (short) (low & 0xff);
            short s1 = (short) (high & 0xff);

            s1 <<= 8;
            short length = (short) (s0 | s1);

            //获取包头中的protobuf类型
            in.readByte();
            byte dataType = in.readByte();

            //如果可读长度大于body长度，恢复读指针，退出。
            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }

            //读取body，根据包头的body长度读取
            ByteBuf bodyByteBuf = in.readBytes(length);

            byte[] array;
            int offset;

            int readableLen = bodyByteBuf.readableBytes();
            if (bodyByteBuf.hasArray()) {
                array = bodyByteBuf.array();
                offset = bodyByteBuf.arrayOffset() + bodyByteBuf.readerIndex();
            } else {
                array = new byte[readableLen];
                bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), array, 0, readableLen);
                offset = 0;
            }

            //反序列化
            MessageLite result = decodeBody(dataType, array, offset, readableLen);
            out.add(result);
        }
    }


    public MessageLite decodeBody(byte dataType, byte[] array, int offset, int length) throws Exception {
        if (dataType == 0x00) {
            return StudentPb.Student.getDefaultInstance().getParserForType().parseFrom(array, offset, length);
        } else if (dataType == 0x01) {
            return TeacherPb.Teacher.getDefaultInstance().getParserForType().parseFrom(array, offset, length);
        }

        return null;//or throw exception
    }


}
