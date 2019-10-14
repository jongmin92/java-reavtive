package com.jongmin.reactive.practice;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

/**
 * 1. Reactive 관련된 아이디어를 이야기 할 때 Duality(쌍대성)라는 용어가 등장한다.
 * 2. Observer Pattern
 * 3. Reactive Streams - 표준 -> Java9 JDK에 추가됨
 */
@Slf4j
public class Ob {
    // Source -> Event/Data -> Observer
    static class IntObservable extends Observable implements Runnable {

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                setChanged();
                notifyObservers(i);         // push
                // int i = it.next()        // pull
            }
        }
    }

    public static void main(String[] args) {
        // Iterable <---> Observable (duality)
        // Pull     <---> Push
        Observer ob = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                log.info("{}", arg);
            }
        };

        IntObservable io = new IntObservable();
        io.addObserver(ob);

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(io);

        log.info("EXIT");
        es.shutdown();
    }
}
