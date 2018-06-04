package com.yy.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btRxLifecycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RxLifeCycleActivity.class));
            }
        });
        //origial();
        //thread();
        //Map();
        //flatMap();
//        concat();
        //concatMap();
        //Zip();
        //filter();
        //flowable();
        //distinct();
        //buffer();
        //timer();
        //interval();
        // doOnNext();
//        skip();
//    take();
        //  single();
        //debounce();
    }

    private void debounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(0);
                Thread.sleep(400);
                e.onNext(1);
                Thread.sleep(505);
                e.onNext(2);
                Thread.sleep(100);
                e.onNext(3);
                Thread.sleep(600);
                e.onNext(4);
                Thread.sleep(510);
                e.onNext(5);
            }
        })
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer + "");
                    }
                });

    }

    private void single() {
        Single.just(new Random().nextInt()).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer value) {
                Log.d(TAG, "onSuccess" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError->" + e);
            }
        });
    }

    private void take() {
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    private void skip() {
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    private void doOnNext() {
        Observable.just(1, 2, 3, 4)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext->" + integer);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept->" + integer);
                    }
                });
    }

    private void interval() {
        Log.d(TAG, getDate(System.currentTimeMillis()));
        Observable.interval(3, 2, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, aLong + "");
                        Log.d(TAG, getDate(System.currentTimeMillis()));
                    }
                });
    }

    /**
     * @author 杨阳
     * @date 2018/5/31
     */
    private void timer() {
        Log.d(TAG, System.currentTimeMillis() + "");
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, aLong + "");
                        Log.d(TAG, System.currentTimeMillis() + "");
                    }
                });
    }

    private void buffer() {
        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.d(TAG, integers + "");
                    }
                });
    }

    //去重
    private void distinct() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 4)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    private void concat() {
        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                // e.onError(new Exception());
                e.onComplete();
            }
        });
        Observable observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(4);
                e.onNext(5);
                e.onNext(6);
            }
        });
        Observable.concat(observable, observable2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, integer + "accept");
            }
        });
    }

    private void flowable() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "FlowableEmitter->" + e.requested());
                Log.d(TAG, "FlowableEmitter1");
                e.onNext(1);
                Log.d(TAG, "FlowableEmitter2");
                e.onNext(2);
                Log.d(TAG, "FlowableEmitter3");
                e.onNext(3);
                Log.d(TAG, "FlowableEmitter1->onComplete");
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe");
                // s.request(2);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext" + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
    }

    private void filter() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 2000; i++) {
                    e.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                })
                .observeOn(Schedulers.io())
                .sample(2, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept" + integer);
                    }
                });
    }

    private void Zip() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "ObservableEmitter1");
                e.onNext(1);
                Thread.sleep(1000);
                Log.d(TAG, "ObservableEmitter2");
                e.onNext(2);
                Thread.sleep(1000);
                Log.d(TAG, "ObservableEmitter3");
                e.onNext(3);
                Thread.sleep(1000);
                Log.d(TAG, "ObservableEmitter4");
                e.onNext(4);
                Thread.sleep(1000);
//                Log.d(TAG, "onComplete1");
//                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.d(TAG, "ObservableEmitterA");
                e.onNext("A");
                Thread.sleep(1000);
                Log.d(TAG, "ObservableEmitterB");
                e.onNext("B");
                Thread.sleep(1000);
                Log.d(TAG, "ObservableEmitterC");
                e.onNext("C");
                Thread.sleep(1000);
//                Log.d(TAG, "onComplete2");
//                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable.zip(observable, stringObservable, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
    }

    private void concatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    private void flatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    private void Map() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer + "被Map操作符转换";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    private void thread() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "ObservableEmitter1");
                Log.d(TAG, "Observable所在线程： " + Thread.currentThread().getName());
                e.onNext(1);
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext1->" + integer + "->Observer所在线程：" + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext2->" + integer + "->Observer所在线程：" + Thread.currentThread().getName());

                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept" + integer);
                        Log.d(TAG, "Observer所在线程：" + Thread.currentThread().getName());
                    }
                });
    }

    private void origial() {
        // Observable<Integer> observable =
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "ObservableEmitter1");
                e.onNext(1);
                Log.d(TAG, "ObservableEmitter2");
                e.onNext(2);
                e.onError(new Exception());
                Log.d(TAG, "ObservableEmitter3");
                e.onNext(3);
                //  e.onComplete();
                Log.d(TAG, "ObservableEmitter4");
                e.onNext(4);
                // e.onError(new Exception());
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept" + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "accept" + throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG, "Action");
            }
        });
//                        .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        Log.d(TAG, "accept"+integer);
//                    }
//                });
        Observer<Integer> observer = new Observer<Integer>() {
            private int i;
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext" + value);
                i++;
                if (i == 2) {
                    Log.d(TAG, "dispose");
                    mDisposable.dispose();
                    Log.d(TAG, "isDisposed()" + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
        //observable.subscribe(observer);
    }

    private String getDate(Long time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dateFormat.format(new Date(time));
        return dateFormat.format(new Date(time));
    }

}
