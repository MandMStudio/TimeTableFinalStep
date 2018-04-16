package com.mmstudio.timetable.Fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mmstudio.timetable.DBHelper;
import com.mmstudio.timetable.Items.DayItem;
import com.mmstudio.timetable.Items.SubjectItem;
import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class WeekFragment extends Fragment  {
    private int week;
    private int showMode;
    private String[] weekDays= new String[]{DBHelper.TABLE_MON,DBHelper.TABLE_TUE,DBHelper.TABLE_WEN,DBHelper.TABLE_THU,DBHelper.TABLE_FRI,DBHelper.TABLE_SAT,DBHelper.TABLE_SUN};
    LinearLayout content;
    @SuppressLint("ValidFragment")
    public WeekFragment(int week,int showMode) {
        this.week = week;
        this.showMode = showMode;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.week_fragment, container, false);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        content = v.findViewById(R.id.week_content);

        for (int i = 0; i < weekDays.length; i++) {
            new MyTask(i).execute();
        }


        return v;
    }


    class MyTask extends AsyncTask<Void,View,Void>{
        int i;

        public MyTask(int i) {
            this.i = i;
        }

        private View viewToAdd = new View(getActivity());



        @Override
        protected Void doInBackground(Void... voids) {
            if(showMode == DayItem.DONT_SHOW_MODE) {




                    if (DataFragment.readDayFromDB(getActivity(), weekDays[i] + Integer.toString(week)).length != 0) {
                        Log.d("mLog","I am here");
                        viewToAdd = (new DayItem(getActivity(), weekDays[i], week, DataFragment.readDayFromDB(getActivity(), weekDays[i] + Integer.toString(week)), SubjectItem.NONE_ANIM_MODE).getView());
                    }else {
                        viewToAdd = (new DayItem(getActivity(), weekDays[i], week, DataFragment.readDayFromDB(getActivity(), weekDays[i] + Integer.toString(week)), showMode).getView());
                    }

                }else

                    viewToAdd = ( new DayItem(getActivity(),weekDays[i],week,DataFragment.readDayFromDB(getActivity(), weekDays[i] + Integer.toString(week)),showMode).getView());








            return null;
        }
        @Override
        protected void onPostExecute(Void voids) {

                content.addView(viewToAdd);


        }

    }

    }
