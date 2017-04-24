package com.asdf.luo4.Actions;

import com.asdf.luo4.Redux.Action.Action;

/**
 * Created by asdf on 2017/4/22.
 */

public class PauseDownloadAction implements Action {
    public static final String TYPE = "PAUSE_DOWNLOAD";

    private int id;

    public PauseDownloadAction(int id) {
        this.id = id;
    }

    public int getId() {return this.id;}
    public String getType() {
        return TYPE;
    }
}
