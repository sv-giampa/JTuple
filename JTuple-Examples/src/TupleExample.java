/*
 *
 * Copyright 2020 Salvatore Giampa'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import jtuple.Tuple;

import java.util.TreeSet;

public class TupleExample {
    public static void main(String[] args) {
        Tuple tuple = Tuple.of(5, 7, 2.3, 9);
        System.out.println("values = " + tuple);
        System.out.println();

        System.out.print("for-each on values: ");
        for (Object v : tuple) {
            Number n = (Number) v;
            System.out.print(n + "  ");
        }
        System.out.println();
        System.out.println();

        System.out.print("print values as varargs of Number: ");
        print(tuple.toArray(Number.class));
        System.out.println();

        System.out.print("printf(\"x=%s, y=%s, z=%s\", values.toArray()):\t\t");
        System.out.printf("x=%s, y=%s, z=%s", tuple.toArray());
        System.out.println();
        System.out.println();

        System.out.println("compareTo samples:");
        Tuple t1 = Tuple.of("hell", "o", "<t1>");
        Tuple t2 = Tuple.of("hello", 2, "<t2>");
        Tuple t3 = Tuple.of(2, 3, "<t3>");
        Tuple t4 = Tuple.of(10, "<t4>");

        System.out.println("t1 = " + t1);
        System.out.println("t2 = " + t2);
        System.out.println("t3 = " + t3);
        System.out.println("t4 = " + t4);
        System.out.println();
        System.out.println("t1.hashCode() = " + t1.hashCode());
        System.out.println("t2.hashCode() = " + t2.hashCode());
        System.out.println("t3.hashCode() = " + t3.hashCode());
        System.out.println("t4.hashCode() = " + t4.hashCode());
        System.out.println();
        System.out.println("compareTo:");
        System.out.println("t1.compareTo(t2)=" + t1.compareTo(t2));
        System.out.println("t1.compareTo(t3)=" + t1.compareTo(t3));
        System.out.println("t2.compareTo(t3)=" + t2.compareTo(t3));
        System.out.println();
        System.out.println("reverse compareTo:");
        System.out.println("t2.compareTo(t1)=" + t2.compareTo(t1));
        System.out.println("t3.compareTo(t1)=" + t3.compareTo(t1));
        System.out.println("t3.compareTo(t2)=" + t3.compareTo(t2));
        System.out.println();

        TreeSet<Tuple> set = new TreeSet<Tuple>();
        set.add(t1);
        set.add(t2);
        set.add(t3);
        set.add(t4);
        System.out.println("sorted tuples: " + set);
        System.out.println();

        t1 = Tuple.of("hello");
        System.out.println("t1 = " + t1);
        System.out.println("t1.add(\"world\", \"!\"): " + t1.add("world", "!"));
        System.out.println("t1 = " + t1);
        System.out.println();

        t1 = Tuple.of("hi");
        t2 = Tuple.of("Geralt");
        System.out.println("t1 = " + t1);
        System.out.println("t2 = " + t2);
        System.out.println("t1.add(t2): " + t1.concat(t2));
        System.out.println("t1 = " + t1);
        System.out.println("t2 = " + t2);
        System.out.println();

        t1 = Tuple.of(35, "Geralt", "of", "Rivia", 3.564);
        System.out.println("t1 = " + t1);
        System.out.println("t1.head() = " + t1.head());
        System.out.println("t1.tail() = " + t1.tail());
        System.out.println();
    }

    private static void print(Number... numbers) {
        for (Number n : numbers)
            System.out.print(n + "  ");
        System.out.println();
    }
}
