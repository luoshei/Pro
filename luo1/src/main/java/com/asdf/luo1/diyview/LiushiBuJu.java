package com.asdf.luo1.diyview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asdf on 2017/4/17.
 */

public class LiushiBuJu extends ViewGroup{

    private ArrayList<ArrayList<View>> allViews = new ArrayList<>();
    private List<Integer> mLineHeight = new ArrayList<Integer>();
    public LiushiBuJu(Context context) {
        this(context,null);
    }

    public LiushiBuJu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LiushiBuJu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.argb(88,0,0xff,0));
    }
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//测量确定容器的宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**--处理测量模式为Exact的时候--**/
        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        for(int i = 0,n = getChildCount();i < n;i++){
            View child = this.getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = lp.leftMargin + lp.rightMargin + child.getMeasuredWidth();
            int childHeight = lp.topMargin + lp.bottomMargin + child.getMeasuredHeight();

            if(lineWidth + childWidth > widthSize){//超出当前行
                width = Math.max(lineWidth, childWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            }else{
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight,childHeight);
            }

            if(i == n - 1){
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize
                : width, (heightMode == MeasureSpec.EXACTLY) ? heightSize
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        allViews.clear();
        mLineHeight.clear();
        int lineHeight = 0;
        int lineWidth = 0;
        ArrayList<View> lineViews = new ArrayList<>();
        for(int i = 0,n = getChildCount();i < n;i++){
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            if(lineWidth + lp.leftMargin + lp.rightMargin + childWidth > getWidth()){
                mLineHeight.add(lineHeight);
                allViews.add(lineViews);
                lineWidth = 0;
                lineViews = new ArrayList<>();
            }
                lineWidth += lp.leftMargin + lp.rightMargin + childWidth;
                lineHeight = Math.max(lineHeight,childHeight + lp.topMargin + lp.bottomMargin);
                lineViews.add(child);
        }
        // 记录最后一行
        mLineHeight.add(lineHeight);
        allViews.add(lineViews);

        int left = 0;
        int top = 0;
        for (int i = 0,n = allViews.size();i<n;i++){
            ArrayList<View> views = allViews.get(i);
            for (int j = 0,m = views.size();j<m;j++){
                View c = views.get(j);
                MarginLayoutParams lp = (MarginLayoutParams) c.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + c.getMeasuredWidth();
                int bc = tc + c.getMeasuredHeight();
                c.layout(lc,tc,rc,bc);
                left += c.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            left = 0;
            top += mLineHeight.get(i);
        }
    }
}
