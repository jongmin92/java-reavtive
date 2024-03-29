package com.jongmin.reactive.practice;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

/**
 * Observer Pattern에 부족한 부분 2가지
 * 1. Complete의 개념이 부족
 * 2. 예외가 전파되는 방식, 예외를 어떻게 처리할 것인가에 대한 부분
 * [reactive-streams](http://www.reactive-streams.org)는 이런 부족한 부분을 해결하는 답을 갖고 있다.
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
