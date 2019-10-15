package com.jongmin.reactive.practice;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;

/**
 * Publisher -> [Data1] -> Operator1 -> [Data2]-> Operator2 -> [Data3] -> Subscriber
 * 1. map (d1 -> f -> d2)
 *      pub -> [Data1] -> mapPub -> [Data2]  -> logSub
 */
@Slf4j
public class PubSub2 {
    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1)
                                               .limit(10)
                                               .collect(Collectors.toList()));
        Publisher<Integer> mapPub = mapPub(pub, s -> s * 10);
//        Publisher<Integer> sumPub = sumPub(pub);
//        Publisher<Integer> reducePub = reducePub(pub, 0, (a, b) -> a + b);
        mapPub.subscribe(logSub());
    }

    /*
    private static Publisher<Integer> reducePub(Publisher<Integer> pub, int init,
                                                BiFunction<Integer, Integer, Integer> bf) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new DelegateSub(sub) {
                    int result = init;

                    @Override
                    public void onNext(Integer i) {
                        result = bf.apply(result, i);
                    }

                    @Override
                    public void onComplete() {
                        sub.onNext(result);
                        sub.onComplete();
                    }
                });
            }
        };
    }
    */

    /*
    private static Publisher<Integer> sumPub(Publisher<Integer> pub) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new DelegateSub(sub) {
                    int sum = 0;

                    @Override
                    public void onNext(Integer i) {
                        sum += i;
                    }

                    @Override
                    public void onComplete() {
                        sub.onNext(sum);
                        sub.onComplete();
                    }
                });
            }
        };
    }
    */

    private static <T> Publisher<T> mapPub(Publisher<T> pub, Function<T, T> f) {
        return new Publisher<T>() {
            @Override
            public void subscribe(Subscriber<? super T> sub) {
                pub.subscribe(new DelegateSub<T>(sub) {
                    @Override
                    public void onNext(T i) {
                        sub.onNext(f.apply(i));
                    }
                });
            }
        };
    }

    private static Subscriber<Integer> logSub() {
        return new Subscriber<Integer>() {
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
    }

    private static Publisher<Integer> iterPub(List<Integer> iter) {
        return new Publisher<Integer>() {
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
    }
}
