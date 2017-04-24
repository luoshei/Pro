package com.asdf.luo4.Actions;

import android.app.Service;

import com.asdf.luo4.Redux.Action.Action;

/**
 * Created by asdf on 2017/4/22.
 */

public class StartServiceAction implements Action {
    public static final String TYPE = "START_SERVICE";

    private Class<? extends Service> sClass;
    private String action;

    public StartServiceAction(Class<? extends Service> sClass, String action) {
        this.sClass = sClass;
        this.action = action;
    }

    public Class<? extends Service> getService() {
        return this.sClass;
    }

    public String getType() {
        return TYPE;
    }

    public String getAction() {
        return action;
    }
}
