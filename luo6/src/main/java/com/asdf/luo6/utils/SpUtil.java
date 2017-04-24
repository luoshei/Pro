package com.asdf.luo6.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * Created by asdf on 2017/4/23.
 */

public class SpUtil {
    private static SpUtil spUtil = new SpUtil();
    private SharedPreferences sp;

    public static SpUtil getInstance(){
        return spUtil;
    }
    public static void register(Context context){
        spUtil.sp = context.getSharedPreferences("info",Context.MODE_PRIVATE);
    }

    public String getString(String key){
        String s = sp.getString(key,null);
        return s;
    }

    public int[] getFz(String key){
        int[] downup = new int[2];
        String fz = sp.getString(key,null);
        if(fz == null) return null;
        else {
            String[] sin = fz.split(",");
            downup[0] = Integer.parseInt(sin[0]);
            downup[1] = Integer.parseInt(sin[1]);
            return downup;
        }
    }

    public void setString(String key,String val){
        Editor editor = this.sp.edit();
        editor.putString(key,val);
        editor.commit();
    }
}
