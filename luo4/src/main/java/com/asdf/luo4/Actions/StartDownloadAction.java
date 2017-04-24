package com.asdf.luo4.Actions;

import com.asdf.luo4.Redux.Action.Action;

/**
 * Created by asdf on 2017/4/22.
 */

public class StartDownloadAction implements Action {

    public static final String TYPE = "START_DOWNLOAD";

    private DownloadAction action;

    public StartDownloadAction(DownloadAction action) {
        this.action = action;
    }

    public DownloadAction getAction() {
        return this.action;
    }

    public String getType() {
        return TYPE;
    }
}
