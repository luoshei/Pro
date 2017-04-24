package com.asdf.luo4.Actions;

import com.asdf.luo4.Redux.Action.Action;

/**
 * Created by asdf on 2017/4/22.
 */

public class ToastAction implements Action {
    public static final String TYPE = "TOAST";

    private String msg;

    public ToastAction(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return TYPE;
    }

    public String getMsg() {
        return msg;
    }
}
