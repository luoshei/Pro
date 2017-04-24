package com.asdf.luo1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.widget.TextView;

/**
 * Created by asdf on 2017/4/17.
 */

public class MyTextView extends TextView{
    private GestureDetector gd;

    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTextView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
