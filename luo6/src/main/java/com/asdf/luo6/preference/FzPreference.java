package com.asdf.luo6.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.asdf.luo6.R;
import com.asdf.luo6.utils.SpUtil;

import java.lang.reflect.Field;

import static android.content.ContentValues.TAG;

/**
 * Created by asdf on 2017/4/23.
 */

public class FzPreference extends DialogPreference{

    private String key;
    private int[] sx = new int[2];
    private EditText up;
    private EditText down;
    private Dialog dialog;
    public FzPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        key = getKey();
    }

    public FzPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FzPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        this.dialog = getDialog();
        Log.e(TAG, "onCreateDialogView: " + this.dialog );
        up = (EditText) this.dialog.findViewById(R.id.fz_up);
        down = (EditText) this.dialog.findViewById(R.id.fz_down);

        String fz = SpUtil.getInstance().getString(this.key);
        if(fz!=null){
            String[] downup = fz.split(",");
            sx[0] = Integer.parseInt(downup[0]);
            sx[1] = Integer.parseInt(downup[1]);
            down.setText(downup[0]);
            up.setText(downup[1]);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which){
            case DialogInterface.BUTTON_NEGATIVE:
                setDialogState(true);
                break;
            case DialogInterface.BUTTON_POSITIVE:
                String cur = down.getText().toString() + "," + up.getText().toString();
                if(check(cur)){
                    SpUtil.getInstance().setString(this.key,cur);
                    setDialogState(true);
                }else{
                    setDialogState(false);
                }
                break;
        }
    }

    public void setDialogState(boolean state){
        try {
            Field field = this.dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.setBoolean(this.dialog,state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean check(String cur){
        boolean ok = false;
        if(cur.length()<3) {
            Toast.makeText(getContext(), "上限或下限不能为空", Toast.LENGTH_SHORT).show();
        }else {
            String[] downup = cur.split(",");
            sx[0] = Integer.parseInt(downup[0]);
            sx[1] = Integer.parseInt(downup[1]);
            if (sx[1] <= sx[0]) {
                Toast.makeText(getContext(), "上限必须大于下限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "设置成功", Toast.LENGTH_SHORT).show();
                ok = true;
            }
        }
        return ok;
    }
}
