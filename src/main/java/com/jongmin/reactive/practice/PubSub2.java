package com.jongmin.reactive.practice;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PubSub2 {
    public static void main(String[] args) {
        Publisher<Integer> pub = new Publisher<Integer>() {
            Iterable<Integer> iter = Stream.iterate(1, a -> a + 1)
                                           .limit(10)
                                           .collect(Collectors.toList());

            @Override
            public void subscribe(Subscriber sub) {
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        iter.forEach(i -> sub.onNext(i));
                        sub.onComplete();
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> sub = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.info("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer i) {
                log.info("onNext: {}", i);
            }

            @Override
            public void onError(Throwable t) {
                log.info("onError", t);
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };

        pub.subscribe(sub);
    }
}
