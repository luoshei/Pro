package com.asdf.luo2.custom.view;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by asdf on 2017/4/19.
 */

public class GestureView extends View {

    private ArrayList<Path> paths;
    private long [] timestamp;
    private Paint paint;

    public GestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(2);

        paths = new ArrayList<>();

    }

    public GestureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GestureView(Context context) {
        this(context,null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Path path: paths) {
            canvas.drawPath(path,paint);
        }

    }

    public void setPath(Gesture gesture){

        final ArrayList<GestureStroke> gss = gesture.getStrokes();

        new Thread(new Runnable() {
            @Override
            public void run() {


                for (GestureStroke gs : gss){
                    final float[] localPoints = gs.points;
                    final int count = localPoints.length;
                    try {
                        Field timestamps = gs.getClass().getDeclaredField("timestamps");
                        timestamps.setAccessible(true);
                        timestamp = (long[]) timestamps.get(gs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    float mX = 0;
                    float mY = 0;
                    Path path = null;
                    for (int i = 0; i < count; i += 2) {
                        float x = localPoints[i];
                        float y = localPoints[i + 1];
                        if (path == null) {
                            path = new Path();
                            path.moveTo(x, y);
                            mX = x;
                            mY = y;
                            paths.add(path);
                        } else {
                            float dx = Math.abs(x - mX);
                            float dy = Math.abs(y - mY);
                            if (dx >= 3 || dy >= 3) {
                                path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                                mX = x;
                                mY = y;
                                postInvalidate();
                                if(i < timestamp.length-1) {
                                    Log.e(TAG, "run: " + (timestamp[i + 1] - timestamp[i]));
                                    try {
                                        Thread.sleep(timestamp[i + 1] - timestamp[i]);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                }


            }
        }).start();



    }


}
