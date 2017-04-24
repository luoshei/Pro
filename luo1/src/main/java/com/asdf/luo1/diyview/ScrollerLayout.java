package com.asdf.luo1.diyview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by asdf on 2017/4/18.
 */

/**
 * Created by guolin on 16/1/12.
 */
public class ScrollerLayout extends ViewGroup {

    private Scroller mScroller;
    private int mScrollTouch;

    private int leftBorder;
    private int rightBorder;

    private int page;
    private Timer timer;
    private TimerTask tt;
    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mScrollTouch = ViewConfiguration.get(context).getScaledPagingTouchSlop();
        timer = new Timer();
        tt = initTask();
        timer.schedule(tt,1000,1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for(int i = 0,length = getChildCount();i<length;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //给子View设置位置
        for(int i = 0,length = getChildCount();i<length;i++){
            View child = getChildAt(i);
            child.layout(child.getMeasuredWidth() * i,0,child.getMeasuredWidth() * (i+1),child.getMeasuredHeight());
        }
        leftBorder = getChildAt(0).getLeft();
        rightBorder = getChildAt(getChildCount() - 1).getRight();
    }


    int mDownX;//按下时的X坐标
    int mLastScrollX;
    int mScrollX;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: " );
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getRawX();
                mLastScrollX = mDownX;
                tt.cancel();
                break;
            case MotionEvent.ACTION_MOVE://判断是否需要拦截触摸事件。return true则为拦截
                if(Math.abs((int) ev.getRawX() - mDownX )> mScrollTouch)return true;
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " );
        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:
                int cur = (int) event.getRawX();
                int scrolledX = (int) (mLastScrollX - cur);

                if (getScrollX() + scrolledX < leftBorder) {//用来保证不超过边界
                    scrollTo(leftBorder, 0);
                    return true;
                } else if (getScrollX() + getWidth() + scrolledX > rightBorder) {
                    scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }

                scrollBy(scrolledX,0);
                mLastScrollX = cur;
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent: " + getScrollX());
               int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                page = targetIndex;
                int dx = targetIndex * getWidth() - getScrollX();
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                tt = initTask();
                timer.schedule(tt,1000,1000);
                break;

        }
        return super.onTouchEvent(event);
    }
    public TimerTask initTask(){
        return new TimerTask() {
            @Override
            public void run() {
                page = ++page > 4 ? 0 : page;
                int width = getChildAt(0).getWidth();
                mScroller.startScroll(page == 0 ? -width : getScrollX(), 0, page == 0 ? width : (page * width - getScrollX()), 0);
                postInvalidate();
            }
        };
    }

    @Override
    public void computeScroll() {
        Log.e(TAG, "computeScroll:" );
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }


}