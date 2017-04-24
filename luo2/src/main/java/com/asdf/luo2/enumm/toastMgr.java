package com.asdf.luo2.enumm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by asdf on 2017/4/20.
 */

public enum toastMgr {
    builder;

    private View v;
    private Toast it;

    public void init(Context c) {
        LayoutInflater inflate =(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = Toast.makeText(c, "", Toast.LENGTH_SHORT).getView();
        it = new Toast(c);
        it.setView(v);
    }

    public void display(CharSequence text, int duration) {
        it.setText(text);
        it.setDuration(duration);
        it.show();
    }

    public void display(int Resid, int duration) {
        it.setText(Resid);
        it.setDuration(duration);
        it.show();
    }

}
