package com.asdf.luo6;

import android.app.Application;

import com.asdf.luo6.db.MySqlitDb;
import com.asdf.luo6.utils.SpUtil;

/**
 * Created by asdf on 2017/4/23.
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SpUtil.register(this);
        MySqlitDb.register(this);
    }
}
