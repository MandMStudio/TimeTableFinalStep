package com.mmstudio.timetable.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

@SuppressLint("ValidFragment")
public class WeekFragment extends Fragment {
    private String week;

    @SuppressLint("ValidFragment")
    public WeekFragment(String week) {
        this.week = week;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.week_fragment, container, false);
        MainActivity.currentFragment = "MainFragment";
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ((TextView) v.findViewById(R.id.week)).setText(week);


        return v;
    }

    }
