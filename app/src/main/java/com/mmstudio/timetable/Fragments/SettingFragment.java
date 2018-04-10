package com.mmstudio.timetable.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.mmstudio.timetable.DBHelper;
import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

public class SettingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Switch timeMode = v.findViewById(R.id.timeMode);

        timeMode.setChecked(is12Mode());


       timeMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   //12 hour mode
                   DataFragment.replaceDataToDB(getActivity(), DBHelper.TABLE_SETTINGS,"24mode","12mode");
               }else{
                   //24 hour mode
                   DataFragment.replaceDataToDB(getActivity(), DBHelper.TABLE_SETTINGS,"12mode","24mode");
               }
           }
       });


        return v;
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
