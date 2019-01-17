package com.czh.study.leetcode.pro2;

/**
 * @Auther caizhenghao
 * @Description
 * @Date Create in 2019/1/18 12:27 AM
 * @Modified by
 */
public class AddTwoNumbers {

    public static void main(String[] args) {

    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int add = 0;
        ListNode sumBegin = new ListNode(0);
        ListNode sumLn = sumBegin;
        while (l1 != null || l2 != null) {
            sumLn.next = new ListNode(0);
            sumLn = sumLn.next;

            int sumTemp;
            if (l1 != null && l2 != null) {
                sumTemp = l1.val + l2.val + add;
                l1 = l1.next;
                l2 = l2.next;
            } else if (l1 != null) {
                sumTemp = l1.val + add;
                l1 = l1.next;
            } else {
                sumTemp = l2.val + add;
                l2 = l2.next;
            }

            if (sumTemp > 9) {
                add = 1;
                sumLn.val = sumTemp - 10;
            } else {
                add = 0;
                sumLn.val = sumTemp;
            }
        }

        if (add > 0) {
            sumLn.next = new ListNode(add);
        }

        //这里是非常机智的一个做法，可以避免多余的初次判断
        return sumBegin.next;
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
