package com.jongmin.reactive.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchedulerEx {
    public static void main(String[] args) {
        Publisher<Integer> pub = new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        log.info("request");
                        sub.onNext(1);
                        sub.onNext(2);
                        sub.onNext(3);
                        sub.onNext(4);
                        sub.onNext(5);
                        sub.onComplete();
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

//        Publisher<Integer> subOnPub = new Publisher<Integer>() {
//            @Override
//            public void subscribe(Subscriber<? super Integer> sub) {
//                ExecutorService es = Executors.newSingleThreadExecutor();
//                es.execute(() -> pub.subscribe(sub));
//            }
//        };

        Publisher<Integer> pubOnPub = new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new Subscriber<Integer>() {
                    ExecutorService es = Executors.newSingleThreadExecutor();

                    @Override
                    public void onSubscribe(Subscription s) {
                        sub.onSubscribe(s);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        es.execute(() -> sub.onNext(integer));
                    }

                    @Override
                    public void onError(Throwable t) {
                        es.execute(() -> sub.onError(t));
                    }

                    @Override
                    public void onComplete() {
                        es.execute(() -> sub.onComplete());
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
            public void onNext(Integer integer) {
                log.info("onNext: {}", integer);
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

        pubOnPub.subscribe(sub);

        log.info("exit");
    }
}
