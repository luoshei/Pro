package com.asdf.luo6;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl,new SettingFragment());
        ft.commit();
    }
}
