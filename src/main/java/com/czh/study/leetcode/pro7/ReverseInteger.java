package com.czh.study.leetcode.pro7;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther caizhenghao
 * @Description
 * @Date Create in 2019/1/22 11:38 PM
 * @Modified by
 */
public class ReverseInteger {

    public static void main(String[] args) {
//        System.out.println(Integer.MAX_VALUE/10);
//        System.out.println(Integer.MIN_VALUE/10);
        //float有-0概念
        List<Integer> numbers = new ArrayList<>();
        int b = Float.floatToIntBits(-0.0f);
        for (int i = 0; i < 32; i++) {
            numbers.add(b & 1);
            b = b >>> 1;
        }
        for (int i = numbers.size() - 1; i >= 0; i--) {
            System.out.print(numbers.get(i));
        }

        System.out.println("");
        System.out.println(-0f);

        System.out.println(Integer.MIN_VALUE - 1 == -0);//java整型不存在负0
        System.out.println(new ReverseInteger().reverse(10));
    }

    public int reverse(int x) {
        //0x10000000这个值是int的最小负数，而不是-0
        int xNow = x;
        int temp;
        int result = 0;
        while (xNow != 0) {
            temp = xNow % 10;
            xNow = xNow / 10;
            //最大值的最后一位是7，最小值最后一位是8，一旦超出这个值，必然溢出
            if (result > Integer.MAX_VALUE / 10
                    || (result == Integer.MAX_VALUE && temp > 7)) {
                return 0;
            } else if (result < Integer.MIN_VALUE / 10
                    || (result == Integer.MIN_VALUE && temp < -8)) {
                return 0;
            }
            result = result * 10 + temp;
        }
        //可以使用长整型存储数据，然后判断，白板写代码只能这种写法
        // return result > Integer.MAX_VALUE || result < Integer.MIN_VALUE? 0 : (int)result;
        return result;
    }

}
