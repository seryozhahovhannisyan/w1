package transaction;

import java.util.*;

/**
 * Created by Serozh on 1/30/2017.
 */
public class Test {

    public static void main(String[] args) {
        cast();
        List<String> stringList;
        ArrayList arrayList;
        LinkedList linkedList;
        TreeSet treeSet ;
        Iterator iterator;
        ListIterator listIterator;
        HashMap map;
    }

    public static void cast() {
        int i = 150;
        byte b = 2;


        System.out.println((byte)i);
        System.out.println((short)i);
        System.out.println((long) i);
        System.out.println((float) i);
        System.out.println((double) i);
        System.out.println((char) i);
        System.out.println("***********");
        System.out.println((int) b);
        System.out.println((short)b);
        System.out.println((long) b);
        System.out.println((float) b);
        System.out.println((double) b);
        System.out.println((char) b);
    }

    public static void autoBoxing() {
        int i = 5;
        int i2 = 5;
        int i3 = i;
        Integer integer = 5;
        Integer integer2 = new Integer(5);
        Integer integer3 = integer;

        System.out.println(i == integer);

        System.out.println(i == i2);
        System.out.println(i == i3);

        System.out.println(integer == integer2);
        System.out.println(integer == integer3);

    }

    public static void mainString() {
        String str1 = "hello";
        String str2 = "hello";
        String str3 = "hello".intern();

        String s1 = new String("Cat");
        String s2 = new String("Cat");
        System.out.println(s1 == s2);
        System.out.println(s1.hashCode() + "");
        System.out.println(s2.hashCode() + "");
        if (str1 == str2) {
            System.out.println("str1 and str2 are same");
        }
        if (str1 == str3) {
            System.out.println("str1 and str3 are same");
        }
    }
}
