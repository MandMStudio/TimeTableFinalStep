package com.mmstudio.timetable.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;
import com.mmstudio.timetable.ViewPagerAdapter;

public class MainFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        MainActivity.currentFragment = "MainFragment";
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        tabLayout = getActivity().findViewById(R.id.tabs);
        tabLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

        if(!getActivity().getPreferences(Context.MODE_PRIVATE).getBoolean("twoWeeksMode",false)){

            tabLayout.getLayoutParams().height=0;
            Log.d("myLog","oneWeek");
        }else {
            Log.d("myLog","twoWeek");
           tabLayout.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;
        }



        viewPager = v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);



        tabLayout.setupWithViewPager(viewPager);




        return v;
    }




    @Override
    public void onDestroyView () {
        super.onDestroyView();
        tabLayout.getLayoutParams().height=0;


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new WeekFragment("FirstWeek"), "Firs Week");
        adapter.addFragment(new WeekFragment("SecondWeek"), "Second Week");
        viewPager.setAdapter(adapter);
    }


}
