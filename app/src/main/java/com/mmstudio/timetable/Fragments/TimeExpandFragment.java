package com.mmstudio.timetable.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mmstudio.timetable.DBHelper;
import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

public class TimeExpandFragment extends Fragment {
    private String oldValue="";
    FloatingActionButton fab;
    TimePicker tmStart;
    TimePicker tmEnd;


    public TimeExpandFragment() {
    }

    @SuppressLint("ValidFragment")
    public TimeExpandFragment(String oldValue) {
        this.oldValue = oldValue;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.time_expands_fragment, container, false);

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        MainActivity.currentFragment = "TimeExpandFragment";

       tmStart = v.findViewById(R.id.startingTimePicker);
       tmEnd = v.findViewById(R.id.endingTimePicker);
        if(!oldValue.equals("")){
            int[] timesOldValues = timeElements(oldValue);
            tmStart.setCurrentHour(timesOldValues[0]);
            tmStart.setCurrentMinute(timesOldValues[1]);
            tmEnd.setCurrentHour(timesOldValues[2]);
            tmEnd.setCurrentMinute(timesOldValues[3]);
        }

        tmStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                int lastMinute = minute;
                int minuteToAdd=0;
                int lastHour = hourOfDay;
                int hourToAd=0;
                lastMinute +=Integer.parseInt(MainActivity.appSettings.get(1));
                if(lastMinute>60){
                    hourToAd = (int) Math.floor(lastMinute/60);
                    minute = lastMinute - hourToAd*60;
                }
                lastHour+=hourToAd;
                lastMinute+=minuteToAdd;


                tmEnd.setCurrentMinute(lastMinute);
                tmEnd.setCurrentHour(lastHour);

            }
        });

        fab = v.findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceType"})
            @Override
            public void onClick(View v) {
                String newValue = tmStart.getCurrentHour() + ":" + tmStart.getCurrentMinute() + "-" + tmEnd.getCurrentHour() + ":" + tmEnd.getCurrentMinute();
                if(oldValue.equals("")) {
                    if(DataFragment.readFromDB(getActivity(),DBHelper.TABLE_TIME).contains(newValue)){
                        Toast.makeText(getActivity(),R.string.already_exist,Toast.LENGTH_SHORT).show();
                    }else {
                        DataFragment.addDataToDB(getActivity(), DBHelper.TABLE_TIME, newValue);
                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_right_enter, R.anim.slide_right_exit).replace(R.id.content_frame, new DataFragment(DBHelper.TABLE_TIME)).commit();
                    }
                    }else{
                    if(oldValue.equals(newValue)){
                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_right_enter, R.anim.slide_right_exit).replace(R.id.content_frame, new DataFragment(DBHelper.TABLE_TIME)).commit();
                    }else{
                        DataFragment.replaceDataToDB(getActivity(),DBHelper.TABLE_TIME,oldValue,newValue);
                        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_right_enter, R.anim.slide_right_exit).replace(R.id.content_frame, new DataFragment(DBHelper.TABLE_TIME)).commit();
                    }
                }
            }
        });



        return v;
    }
private int[] timeElements(String rowString){

        int[] res = new int[4];

        String[] temp = rowString.split("-");

        res[0]=Integer.parseInt(temp[0].split(":")[0]);
        res[1]=Integer.parseInt(temp[0].split(":")[1]);
        res[2]=Integer.parseInt(temp[1].split(":")[0]);
        res[3]=Integer.parseInt(temp[1].split(":")[1]);


        return res;
}

}
