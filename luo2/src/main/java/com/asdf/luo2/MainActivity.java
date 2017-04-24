package com.asdf.luo2;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;

import com.asdf.luo2.custom.view.GestureView;
import com.asdf.luo2.enumm.toastMgr;
import com.asdf.luo2.util.BindIdUtil;
import com.asdf.luo2.util.BindView;

import java.io.File;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {


    @BindView(R.id.gov)
    private GestureOverlayView gov;

    @BindView(R.id.cv)
    private GestureView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindIdUtil.register(this);

        //gestureoverlayview 属性设置
        gov.setFadeEnabled(true);
        gov.setFadeOffset(2000);
        gov.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);

        gov.setGestureColor(getResources().getColor(android.R.color.holo_red_dark));
        gov.setUncertainGestureColor(getResources().getColor(android.R.color.holo_blue_dark));

        gov.setGestureStrokeWidth(4);
        gov.invalidate();
        toastMgr.builder.init(this);
        gov.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                /*Toast.makeText(MainActivity.this, "绘制完成", Toast.LENGTH_SHORT).show();
                saveGesture(gesture);*/
                GestureLibrary gl = GestureLibraries.fromRawResource(MainActivity.this,R.raw.gesture);
                gl.load();//这一句非常重要，获取到GestureLibrary对象时，须调用其load方法进行加载手势
                ArrayList<Prediction> predictions = gl.recognize(gesture);
                if (predictions.size() > 0) {
                    Prediction prediction = (Prediction) predictions.get(0);
                    Log.e(TAG, "onGesturePerformed: pre" + prediction);
                    // 匹配的手势
                    if (prediction.score > 1.0) { // 越匹配score的值越大，最大为10
                        CountDownTimer cdt = new CountDownTimer(5000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.e(TAG, "onTick: cur:" + Thread.currentThread().getName() );

                                toastMgr.builder.display(millisUntilFinished / 1000 + "秒后关闭", 0);
                            }

                            @Override
                            public void onFinish() {
                                MainActivity.this.finish();
                            }
                        };
                        cdt.start();
                    }

                }



            }
        });


        Gesture gesture = loadGesture();
            gv.setPath(gesture);
        gov.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
            @Override
            public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {

            }

            @Override
            public void onGesture(GestureOverlayView overlay, MotionEvent event) {

            }

            @Override
            public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {

            }

            @Override
            public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {

            }
        });


    }

    public void saveGesture(Gesture gesture){
        //获取外部内存卡路径
        File mStoreFile = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            mStoreFile = new File(Environment.getExternalStorageDirectory(),"gesture");
        }
        if(mStoreFile != null){
            GestureLibrary gl = GestureLibraries.fromFile(mStoreFile);
            gl.addGesture("one",gesture);
            gl.save();
        }
    }

    public Gesture loadGesture() {
        File mStoreFile = new File(Environment.getExternalStorageDirectory(), "gesture");
        Gesture gesture = null;
        if (mStoreFile.exists()) {
            GestureLibrary gl = GestureLibraries.fromFile(mStoreFile);
            gl.load();
            ArrayList<Gesture> als = gl.getGestures("one");
            gesture = als.get(0);
        }
        return gesture;
    }

}
