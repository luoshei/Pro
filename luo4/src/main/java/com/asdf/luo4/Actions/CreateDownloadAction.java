package com.asdf.luo4.Actions;

import com.asdf.luo4.Redux.Action.Action;
import com.asdf.luo4.model.NetFileInfo;

/**
 * Created by asdf on 2017/4/22.
 */

public class CreateDownloadAction implements Action {

    public static final String TYPE = "CREATE_DOWNLOAD";

    private NetFileInfo netFileInfo;

    public CreateDownloadAction(NetFileInfo netFileInfo) {
        this.netFileInfo = netFileInfo;
    }

    public NetFileInfo getNetFileInfo() {
        return this.netFileInfo;
    }

    public String getType() {
        return TYPE;
    }
}
