package com.czh.study;

/**
 * @Author: cai.zhenghao
 * @Description: 常见面试题，确定两个单项链表是否有相交的点，以及找到共同节点
 * @Date: Created in 2018/12/12  4:02 PM
 * @Modified By:
 */
public class FindTheCommonNodeInLinke {
    public static void main(String[] args) {
        System.out.println("test");
        new FindTheCommonNodeInLinke().test();
    }


    public void test() {
        Node head1, head2;
        Node headTemp1, headTemp2;

        head1 = new Node("11", null);
        head1.next = new Node("12", null);
        headTemp1 = head1.next;
        headTemp1.next = new Node("13", null);
        headTemp1 = headTemp1.next;
        headTemp1.next = new Node("14", null);
        headTemp1 = headTemp1.next;
        headTemp1.next = new Node("15", null);
        headTemp1 = headTemp1.next;

        head2 = new Node("21", null);
        head2.next = new Node("22", null);
        headTemp2 = head2.next;

        //交叉点22
        headTemp1.next = head2.next;

        headTemp2.next = new Node("23", null);
        headTemp2 = headTemp2.next;
        headTemp2.next = new Node("24", null);
        headTemp2 = headTemp2.next;
        headTemp2.next = new Node("25", null);

        headTemp1 = head1;
        do {
            System.out.print(headTemp1.value + " ");
            headTemp1 = headTemp1.next;
        } while (headTemp1.next != null);
        System.out.println("\n");

        headTemp2 = head2;
        do {
            System.out.print(headTemp2.value + " ");
            headTemp2 = headTemp2.next;
        } while (headTemp2.next != null);
        System.out.println("\n");


        System.out.println(getCommonNode(head1, head2).value);
    }


    public Node getCommonNode(Node head1, Node head2) {
        int length1, length2;

        if (head1 == null || head2 == null) {
            return null;
        }

        Node headTemp1 = head1;
        //找出第一个长度
        length1 = 1;
        while (headTemp1.next != null) {
            headTemp1 = headTemp1.next;
            length1++;
        }

        //找出第二个长度
        Node headTemp2 = head2;
        length2 = 1;
        while (headTemp2.next != null) {
            headTemp2 = headTemp2.next;
            length2++;
        }

        if (headTemp1 != headTemp2) {
            //链表没有相交
            System.out.println("链表没有相交");
            return null;
        }

        System.out.println("length1=" + length1 + ",length2=" + length2);

        Node longLink;
        Node shortLink;

        if (length1 > length2) {
            longLink = head1;
            shortLink = head2;
        } else {
            longLink = head2;
            shortLink = head1;
        }

        //长链移动差额个节点
        for (int i = 0; i < Math.abs(length1 - length2); i++) {
            longLink = longLink.next;
        }

        do {
            if (longLink.value.equals(shortLink.value)) {
                return longLink;
            }
            longLink = longLink.next;
            shortLink = shortLink.next;
        } while (longLink.next != null);

        return null;
    }

    class Node {
        String value;
        Node next;

        Node(String value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

}
