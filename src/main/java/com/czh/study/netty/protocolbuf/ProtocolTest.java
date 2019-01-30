package com.czh.study.netty.protocolbuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2019/1/30  6:55 PM
 * @Modified By:
 */
public class ProtocolTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        ProtoDemo.Student student =  ProtoDemo.Student.newBuilder()
                .setId(100).setName("蔡正浩")
                .setEmail("zhuliangliang.me").build();
        System.out.println(student);
        ProtoDemo.Student student1 = ProtoDemo.Student.parseFrom(student.toByteArray());
        System.out.println(student1.getName());
    }
}
