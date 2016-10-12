package com.zyh.stopmusicdeml;

import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AudioManager.OnAudioFocusChangeListener {

    Button btn_startmusic,btn_stopmusic,btn_testrxjava,btn_testrxandroid;
    AudioManager mAudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_startmusic = (Button) findViewById(R.id.btn_startmusic);
        btn_stopmusic = (Button) findViewById(R.id.btn_stopmusic);
        btn_testrxjava = (Button) findViewById(R.id.btn_testrxjava);
        btn_testrxandroid = (Button) findViewById(R.id.btn_testrxandroid);

        btn_startmusic.setOnClickListener(this);
        btn_stopmusic.setOnClickListener(this);
        btn_testrxjava.setOnClickListener(this);
        btn_testrxandroid.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mAudioManager =(AudioManager)getSystemService(AUDIO_SERVICE);
        switch (view.getId()){
            case R.id.btn_startmusic:
                mAudioManager.abandonAudioFocus(MainActivity.this);
                break;
            case R.id.btn_stopmusic:
                mAudioManager.requestAudioFocus(MainActivity.this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                break;
            case R.id.btn_testrxjava:
                testRxJava();
                break;
            case R.id.btn_testrxandroid:
                testRxAndroid();
                break;
        }
    }

    private void testRxJava(){

        Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext("hahaha");
                subscriber.onNext("I'm");
                subscriber.onNext("your father");
            }
        });

        Subscriber<String> stringSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e("zyhRxjava","onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("zyhRxjava",e.toString());
            }

            @Override
            public void onNext(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                Log.e("zyhRxjava",s);
            }
        };
        Subscriber<Integer> intSubscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.e("zyhRxjava","onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("zyhRxjava",e.toString());
            }

            @Override
            public void onNext(Integer s) {
                Toast.makeText(MainActivity.this, s+"", Toast.LENGTH_SHORT).show();
                Log.e("zyhRxjava",s+"");
            }
        };
        stringObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(stringSubscriber);


        Observable.just("hello","RxJava").skip(1).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("zyhRxJava","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("zyhRxJava",e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        btn_testrxandroid.setText(s);
                    }
        });
    }
    private void testRxAndroid(){
    }

    @Override
    public void onAudioFocusChange(int i) {
        Log.e("zyhLog","change to: "+i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAudioManager.abandonAudioFocus(MainActivity.this);
    }
}
