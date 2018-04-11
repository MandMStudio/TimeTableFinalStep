package com.mmstudio.timetable.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

public class SettingFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.settings_fragment);
        MainActivity.currentFragment="SettingFragment";

        SwitchPreference switchPreference = (SwitchPreference) getPreferenceManager().findPreference("timeMode");
        switchPreference.setChecked(is12Mode());

    }

    public static boolean is12Mode(){
        String rowString = MainActivity.appSettings.get(0);
        if(rowString.equals("24mode")){
            return false;
        }else {
            return true;
        }

    }
}
