package com.jongmin.reactive.practice;

import java.util.Arrays;

/**
 * 1. Reactive 관련된 아이디어를 이야기 할 때 Duality(쌍대성)라는 용어가 등장한다.
 * 2. Observer Pattern
 * 3. Reactive Streams - 표준 -> Java9 JDK에 추가됨
 */
public class Ob {
    public static void main(String[] args) {
        // List > Collection > Iterable
        // Iterable interface를 구현하면 "for-each Loop" 구문의 target이 될 수 있다.
        Iterable<Integer> iter = Arrays.asList(1, 2, 3, 4, 5);
        for (Integer i : iter) {
            System.out.println(i);
        }
    }
}
