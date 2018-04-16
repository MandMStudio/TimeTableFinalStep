package com.mmstudio.timetable.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import com.mmstudio.timetable.DBHelper;
import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Date;

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences sPref;
    SwitchPreferenceCompat is12hourTimeMode;
    SwitchPreferenceCompat twoWeeksMode;
    ListPreference currentWeek;
    boolean isCurrentWeekChanged = false;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        addPreferencesFromResource(R.xml.settings_fragment);

        MainActivity.currentFragment = "SettingFragment";

        twoWeeksMode = (SwitchPreferenceCompat) getPreferenceManager().findPreference("twoWeeksMode");
        is12hourTimeMode = (SwitchPreferenceCompat) getPreferenceManager().findPreference("timeMode");
        currentWeek = (ListPreference) getPreferenceManager().findPreference("currentWeek");



    }

    @Override
    public void onResume(){
        super.onResume();
        sPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
        isCurrentWeekChanged = false;

       currentWeek.setEnabled(twoWeeksMode.isChecked());

        SwitchPreferenceCompat timeMode = (SwitchPreferenceCompat) getPreferenceManager().findPreference("timeMode");
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);



    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("timeMode",is12hourTimeMode.isChecked());
        editor.putBoolean("twoWeeksMode",twoWeeksMode.isChecked());
        editor.commit();
        if(isCurrentWeekChanged){
        updateDateOfFirstWeekMonday();
        }
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);


    }

    private void updateDateOfFirstWeekMonday() {

        LocalDate date = new LocalDate();
        int dayOfWeek = date.getDayOfWeek();
        dayOfWeek += currentWeek.getValue().equals("1") ? 0 : 7;
        if (dayOfWeek != 1) {
            date = date.minusDays(dayOfWeek - 1);
        }
        sPref.edit().putString("dateOfFirstWeekMonday",date.toString());
        sPref.edit().commit();
    }



    public static boolean is12Mode(Activity activity){
        SharedPreferences sPref = activity.getPreferences(Context.MODE_PRIVATE);


        return sPref.getBoolean("timeMode",true);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("twoWeeksMode")){
            currentWeek.setEnabled(twoWeeksMode.isChecked());
            if(twoWeeksMode.isChecked()){
                Toast.makeText(getActivity(),R.string.reminder_for_week_selection,Toast.LENGTH_SHORT).show();
            }
        }

        if(key.equals("currentWeek")){
        isCurrentWeekChanged = true;
        }

    }
}
