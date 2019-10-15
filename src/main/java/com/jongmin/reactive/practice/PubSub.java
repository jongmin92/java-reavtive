package com.jongmin.reactive.practice;

import java.util.Arrays;
import java.util.Iterator;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PubSub {
    public static void main(String[] args) {
        // Publisher  <- Observable
        // Subscriber <- Observer

        /**
         * A Publisher is a provider of a potentially unbounded number of sequenced elements,
         * publishing them according to the demand received from its Subscriber(s).
         *
         * Backpressure(역압): Publisher와 Subscriber 사이의 속도차를 Subscription을 통해서 해결한다.
         */
        Iterable<Integer> iter = Arrays.asList(1, 2, 3, 4, 5);

        Publisher p = new Publisher() {
            @Override
            public void subscribe(Subscriber subscriber) {
                Iterator<Integer> it = iter.iterator();

                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        while(n-- > 0) {
                            if (it.hasNext()) {
                                subscriber.onNext(it.next());
                            } else {
                                subscriber.onComplete();
                                break;
                            }
                        }
                    }

                    @Override
                    public void cancel() {
                        log.info("cancel");
                    }
                });
            }
        };

        Subscriber<Integer> s = new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                log.info("onSubscribe");
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                log.info("onNext: {}", item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                log.info("onError");
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };

        p.subscribe(s);
    }
}
