package com.asdf.luo4.Actions;

import com.asdf.luo4.Redux.Action.Action;

/**
 * Created by asdf on 2017/4/22.
 */

public class DownloadDoneActino implements Action {
    public static final String TYPE = "DOWNLOAD_DONE";
    private int id;
    public DownloadDoneActino(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
    public String getType() {
        return TYPE;
    }
}
