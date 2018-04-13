package com.mmstudio.timetable.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.mmstudio.timetable.DBHelper;
import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

import java.util.Calendar;
import java.util.Date;

public class SettingFragment extends PreferenceFragmentCompat {

    SharedPreferences sPref;
    SwitchPreferenceCompat is12hourTimeMode;
    SwitchPreferenceCompat twoWeeksMode;
    ListPreference currentWeek;



    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        addPreferencesFromResource(R.xml.settings_fragment);

        MainActivity.currentFragment="SettingFragment";

        twoWeeksMode = (SwitchPreferenceCompat) getPreferenceManager().findPreference("twoWeeksMode");
        is12hourTimeMode = (SwitchPreferenceCompat) getPreferenceManager().findPreference("timeMode");
        currentWeek = (ListPreference) getPreferenceManager().findPreference("currentWeek");

    }

    @Override
    public void onResume(){
        super.onResume();

        sPref = getActivity().getPreferences(getContext().MODE_PRIVATE);

       currentWeek.setEnabled(twoWeeksMode.isChecked());

        SwitchPreferenceCompat timeMode = (SwitchPreferenceCompat) getPreferenceManager().findPreference("timeMode");





    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("timeMode",is12hourTimeMode.isChecked());
        editor.putBoolean("twoWeeksMode",twoWeeksMode.isChecked());
        editor.commit();


    }

    public static boolean is12Mode(Activity activity){
        SharedPreferences sPref = activity.getPreferences(Context.MODE_PRIVATE);


        return sPref.getBoolean("timeMode",true);
    }






}
