package com.epam.rd.java.basic.practice2;

public class Demo {

    public static void main(String[] args) {
        Array a = new ArrayImpl(1);
        a.add('A');
        a.add('B');
        a.add('C');
        System.out.println(a.size());
        System.out.println(a);
        a.set(2,'D');
        System.out.println(a);
        a.remove(1);
        System.out.println(a);
        System.out.println(a.indexOf('A'));
        System.out.println(a.size());
    }
}
