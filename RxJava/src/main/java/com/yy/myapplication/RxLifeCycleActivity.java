package com.yy.myapplication;

import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class RxLifeCycleActivity extends RxAppCompatActivity {
    private static final String TAG = "RxLifeCycleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_life_cycle);
//        Observable.interval(1, TimeUnit.SECONDS)
//                .doOnDispose(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        Log.i(TAG, "Unsubscribing subscription from onCreate()");
//                    }
//                })
//                .compose(this.<Long>bindToLifecycle())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long num) throws Exception {
//                        Log.i(TAG, "Started in onCreate(), running until onDestory(): " + num);
//                    }
//                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "Unsubscribing subscription from onResume()");
                    }
                })
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long num) throws Exception {
                        Log.i(TAG, "Started in onResume(), running until in onDestroy(): " + num);
                    }
                });
    }
}
