package com.asdf.luo4.Actions;

import com.asdf.luo4.Redux.Action.Action;
import com.asdf.luo4.model.NetFileInfo;

/**
 * Created by asdf on 2017/4/22.
 */

public class GetFileLenAction implements Action {
    public static final String TYPE = "GET_FILE_LEN";

    private NetFileInfo netFileInfo;

    public GetFileLenAction(NetFileInfo netFileInfo) {
        this.netFileInfo = netFileInfo;
    }

    public String getType() {
        return TYPE;
    }

    public NetFileInfo getNetFileInfo() {
        return netFileInfo;
    }
}
