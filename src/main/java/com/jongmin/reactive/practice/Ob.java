package com.jongmin.reactive.practice;

import java.util.Iterator;

/**
 * 1. Reactive 관련된 아이디어를 이야기 할 때 Duality(쌍대성)라는 용어가 등장한다.
 * 2. Observer Pattern
 * 3. Reactive Streams - 표준 -> Java9 JDK에 추가됨
 */
public class Ob {
    public static void main(String[] args) {
        Iterable<Integer> iter = new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    int i = 0;
                    final static int MAX = 10;

                    @Override
                    public boolean hasNext() {
                        return i < MAX;
                    }

                    @Override
                    public Integer next() {
                        return ++i;
                    }
                };
            }
        };

        for (Integer i : iter) {
            System.out.println(i);
        }
    }
}
