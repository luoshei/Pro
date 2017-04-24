package com.asdf.luo4.Actions;

import com.asdf.luo4.Redux.Action.Action;

/**
 * Created by asdf on 2017/4/22.
 */

public class RemoveDownloadAction implements Action {
    public static final String TYPE = "REMOVE_DOWNLOAD";

    public static final int TYPE_PAUSE = 0;
    public static final int TYPE_STOP = 1;
    private int id;
    private int removeType;

    public RemoveDownloadAction(int id, int type ) {
        this.id = id;
        this.removeType = type;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public int getRemoveType() {
        return removeType;
    }
}
