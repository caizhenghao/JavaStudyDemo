package com.czh.study.netty.protocolbuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2018/12/17  5:49 PM
 * @Modified By:
 */
public class ProtoChatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        super.channelActive(ctx);
//        ctx.writeAndFlush("Hello ,I am server");
        String id = ctx.channel().id().asLongText();
        ProtoChatServer.chMap.put(id,ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("channelRead");
        super.channelRead(ctx, msg);
//        System.out.println("Receive msg from client:" + msg);
        if (msg instanceof StudentPb.Student) {
            StudentPb.Student student = (StudentPb.Student) msg;
            System.out.println("Receive msg from client,is a student ,name is:" + student.getName());
        } else if (msg instanceof TeacherPb.Teacher) {
            TeacherPb.Teacher teacher = (TeacherPb.Teacher) msg;
            System.out.println("Receive msg from client,is a teacher ,name is:" + teacher.getName()
                    + ",nick is:" + teacher.getNick());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        ProtoChatServer.chMap.remove(ctx.channel().id().asLongText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("channelInactive");
        super.channelInactive(ctx);
        ProtoChatServer.chMap.remove(ctx.channel().id().asLongText());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }
}
