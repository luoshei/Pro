package com.asdf.luo6;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by asdf on 2017/4/23.
 */

public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
    }

}
