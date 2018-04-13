package com.mmstudio.timetable.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mmstudio.timetable.DBHelper;
import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeExpandFragment extends Fragment {
    private String oldValue="";
    FloatingActionButton fab;
    TimePicker tmStart;
    TimePicker tmEnd;
    boolean is12HourTimeMode;
    SharedPreferences sp;

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
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);

        MainActivity.currentFragment = "TimeExpandFragment";

       tmStart = v.findViewById(R.id.startingTimePicker);
       tmEnd = v.findViewById(R.id.endingTimePicker);

       tmStart.setIs24HourView(!is12HourTimeMode);
       tmEnd.setIs24HourView(!is12HourTimeMode);

        if(!oldValue.equals("")){
            int[] timesOldValues = timeElements(oldValue);
            tmStart.setCurrentHour(timesOldValues[0]);
            tmStart.setCurrentMinute(timesOldValues[1]);
            tmEnd.setCurrentHour(timesOldValues[2]);
            tmEnd.setCurrentMinute(timesOldValues[3]);
        }else {
            if(!(startTime().length==0)) {
                tmStart.setCurrentHour(startTime()[0]);
                tmStart.setCurrentMinute(startTime()[1]);
            }
        }




        if(!sp.getBoolean("firsTimePick",false)){
            Log.d("firstLog","Firaglafasfgasg");
            tmStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {


                    int timeInMinute = hourOfDay*60+minute;
                    timeInMinute += sp.getInt("timeOfSubject",45);
                    int k=(int) Math.floor(timeInMinute/60);
                    int newHour = k;
                    int newMinute = timeInMinute-k*60;


                    tmEnd.setCurrentMinute(newMinute);
                    tmEnd.setCurrentHour(newHour);

                }
            });
        }

        fab = v.findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceType"})
            @Override
            public void onClick(View v) {
                String newValue = (tmStart.getCurrentHour()==0?"00":tmStart.getCurrentHour()) + ":" + (tmStart.getCurrentMinute()==0?"00":tmStart.getCurrentMinute()) + "-" + (tmEnd.getCurrentHour()==0?"00":tmEnd.getCurrentHour()) + ":" + (tmEnd.getCurrentMinute()==0?"00":tmEnd.getCurrentMinute());
                if(oldValue.equals("")) {
                    if(DataFragment.readFromDB(getActivity(),DBHelper.TABLE_TIME).contains(newValue)){
                        Toast.makeText(getActivity(),R.string.already_exist,Toast.LENGTH_SHORT).show();
                    }else {
                        if(sp.getBoolean("firsTimePick",false)){
                           sp.edit().putBoolean("firsTimePick",true);
                            int timeOfLesson = (tmEnd.getCurrentHour()*60+tmEnd.getCurrentMinute()) - (tmStart.getCurrentHour()*60+tmStart.getCurrentMinute());
                            sp.edit().putInt("timeOfSubject",timeOfLesson);
                            sp.edit().commit();
                        }
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

public int[] startTime(){
        int res[];
        List<String> data = DataFragment.readFromDB(getActivity(),DBHelper.TABLE_TIME);
        if(data.size()==0){
            return new int[0];
        }
        Collections.sort(data, new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return extractInt(o1) - extractInt(o2);
        }

        int extractInt(String s) {
            String num = s.replaceAll("\\D", "");
            // return 0 if no digits found
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }
    });
        String lastData = data.get(data.size()-1);
        String[] temp1 = lastData.split("-");
        String[] temp2 = temp1[1].split(":");
        Log.d("mLog", Arrays.toString(temp2));
        res = new int[]{Integer.parseInt(temp2[0]),Integer.parseInt(temp2[1])};


        return res;
}

}
