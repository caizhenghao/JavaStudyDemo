package com.czh.study.netty.protocolbuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2018/12/17  5:42 PM
 * @Modified By:
 */
public class ProtoChatClientHandler extends ChannelInboundHandlerAdapter {

    public ProtoChatClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // Send the first message if this handler is a client-side handler.
//        ctx.writeAndFlush("Hello ,I am client");
        System.out.println("和服务器的连接建立并激活");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
//        System.out.println("Receive msg from server:" + msg);
        if (msg instanceof StudentPb.Student) {
            StudentPb.Student student = (StudentPb.Student) msg;
            System.out.println("Receive msg from server,is a student ,name is:" + student.getName());
        } else if (msg instanceof TeacherPb.Teacher) {
            TeacherPb.Teacher teacher = (TeacherPb.Teacher) msg;
            System.out.println("Receive msg from server,is a teacher ,name is:" + teacher.getName()
                    + ",nick is:" + teacher.getNick());
        }
//        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
