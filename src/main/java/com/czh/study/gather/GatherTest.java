package com.czh.study.gather;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2019/4/22  4:38 PM
 * @Modified By:
 */
public class GatherTest {

    public static void main(String[] args) {
        Map<String,Integer> map = new ConcurrentHashMap();
        map.computeIfAbsent("test",key->add1(2));
        System.out.println(map.get("test"));

//        map.put("test1",1);
        map.computeIfPresent("test1",(key,value)->add1(value));
        System.out.println(map.get("test1"));
    }

    public static Integer add1(Integer value){
        return value + 5;
    }
}
