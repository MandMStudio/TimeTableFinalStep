package com.mmstudio.timetable.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

public class MainFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        MainActivity.currentFragment = "MainFragment";
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        return v;
    }
}
