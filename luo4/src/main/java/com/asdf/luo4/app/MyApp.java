package com.asdf.luo4.app;

import android.app.Application;

import com.asdf.luo4.Redux.Store.Store;
import com.asdf.luo4.db.MySqlitDb;
import com.asdf.luo4.service.TaskService;

/**
 * Created by asdf on 2017/4/21.
 */

public class MyApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        MySqlitDb.register(this);
        TaskService.init();
        Store.use(null);
        if(android.R.attr.level == 0){

        }
    }
}
