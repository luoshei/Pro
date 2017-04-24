package com.asdf.luo4.Actions;

import com.asdf.luo4.Redux.Action.Action;

/**
 * Created by asdf on 2017/4/22.
 */

public class UpdateProgressAction implements Action {

    public static final String TYPE = "UPDATE_PROGRESS";

    private int progress;

    public UpdateProgressAction(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return this.progress;
    }

    public String getType() {
        return TYPE;
    }
}
