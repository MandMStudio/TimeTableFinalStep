package com.mmstudio.timetable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mmstudio.timetable.Fragments.DataFragment;
import com.mmstudio.timetable.Fragments.MainFragment;
import com.mmstudio.timetable.Fragments.SettingFragment;
import com.mmstudio.timetable.Items.DayItem;
import com.mmstudio.timetable.Items.SubjectItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    public static String currentFragment;//for understanding wich animation need to play
    public static ArrayList<String> subjectTimeData;
    public static ArrayList<String> subjectsData;
    public static ArrayList<String> subjectTeacherData;
    public static ArrayList<String> buildingsData;
    public static ArrayList<String> subjectTypesData;


    Fragment mainFragment;
    Fragment settingsFragment;

    Fragment subjectTimeFragment;
    Fragment subjectFragment;
    Fragment teacherFragment;
    Fragment buildingsFragment;
    Fragment typeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        subjectsData = DataFragment.readFromDB(this,DBHelper.TABLE_SUB);
        subjectTimeData = DataFragment.readFromDB(this,DBHelper.TABLE_TIME);
        subjectTeacherData = DataFragment.readFromDB(this,DBHelper.TABLE_TEACHERS);
        buildingsData = DataFragment.readFromDB(this,DBHelper.TABLE_BUILDINGS);
        subjectTypesData = DataFragment.readFromDB(this,DBHelper.TABLE_TYPE);


       mainFragment = new MainFragment(DayItem.DONT_SHOW_MODE);
       settingsFragment = new SettingFragment();

       subjectTimeFragment = new DataFragment(DBHelper.TABLE_TIME);
       subjectFragment = new DataFragment(DBHelper.TABLE_SUB);
       teacherFragment = new DataFragment(DBHelper.TABLE_TEACHERS);
       buildingsFragment = new DataFragment(DBHelper.TABLE_BUILDINGS);
       typeFragment = new DataFragment(DBHelper.TABLE_TYPE);



        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mainFragment).commit();//On start we want to bew on main fragment

        currentFragment = "MainFragment";

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint({"NewApi", "ResourceType"})
    @Override
    public void onBackPressed() {
        //for closing backside navigation men if opened
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //think where we go on different fragments when we press back button
            switch (currentFragment) {
                case "TimeExpandFragment": //from time picker fragment to time show

                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_right_enter, R.anim.slide_right_exit).replace(R.id.content_frame, subjectTimeFragment).commit();

                    break;

                case "DataFragment"://from any type of data fragment to main fragment

                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down_start, R.anim.slide_down_exit).replace(R.id.content_frame, mainFragment).commit();

                    break;

                case "MainFragment"://if we on main than we build a window which ask us from exiting if program

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.ask_for_exit)
                            .setCancelable(false)
                            .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.super.onBackPressed();

                                }
                            })
                            .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//clicking on slide bar elements
        FragmentManager fm = getSupportFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        //building a priority list to understan to wich side we want animations
        ArrayList<String> fragmenList = new ArrayList<>();
        fragmenList.add(DBHelper.TABLE_TIME);
        fragmenList.add(DBHelper.TABLE_SUB);
        fragmenList.add(DBHelper.TABLE_TEACHERS);
        fragmenList.add(DBHelper.TABLE_BUILDINGS);
        fragmenList.add(DBHelper.TABLE_TYPE);

        String selectionType;

        if (id == R.id.nav_main) {//main has alway hies prioryty tha mean animations goew always down


            if (currentFragment.equals("MainFragment")) {



            } else {

                fm.beginTransaction().setCustomAnimations(R.anim.slide_down_start, R.anim.slide_down_exit).replace(R.id.content_frame, mainFragment).commit();

            }

            setTitle(R.string.app_name);

        } else if (id == R.id.nav_settings) {//setting has second priority. it always have down animation except from main fragment

            if (currentFragment.equals("MainFragment")) {

                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, settingsFragment).commit();

            } else if (currentFragment.equals("DataFragment")) {

                fm.beginTransaction().setCustomAnimations(R.anim.slide_down_start, R.anim.slide_down_exit).replace(R.id.content_frame, settingsFragment).commit();

            } else {

                fm.beginTransaction().replace(R.id.content_frame, settingsFragment).commit();

            }

            this.setTitle(R.string.title_settings);

        } else if (id == R.id.nav_time) {


            selectionType = DBHelper.TABLE_TIME;

            if (currentFragment.equals("SettingFragment")) {

                fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, subjectTimeFragment).commit();

            } else {


                int lastIndex = fragmenList.indexOf(DataFragment.lastSelection);
                int newIndex = fragmenList.indexOf(selectionType);

                if (lastIndex > newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_down_start, R.anim.slide_down_exit).replace(R.id.content_frame, subjectTimeFragment).commit();

                } else if (lastIndex < newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, subjectTimeFragment).commit();

                } else {

                    fm.beginTransaction().replace(R.id.content_frame, subjectTimeFragment).commit();

                }
            }

            this.setTitle(R.string.title_time);

        } else if (id == R.id.nav_subjects) {


            selectionType = DBHelper.TABLE_SUB;

            if (currentFragment.equals("SettingFragment")) {

                fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, subjectFragment).commit();

            } else {

                int lastIndex = fragmenList.indexOf(DataFragment.lastSelection);
                int newIndex = fragmenList.indexOf(selectionType);

                if (lastIndex > newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_down_start, R.anim.slide_down_exit).replace(R.id.content_frame, subjectFragment).commit();

                } else if (lastIndex < newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, subjectFragment).commit();

                } else {

                    fm.beginTransaction().replace(R.id.content_frame, subjectFragment).commit();
                }
            }

            this.setTitle(R.string.title_subjects);

        } else if (id == R.id.nav_teachers) {


            selectionType = DBHelper.TABLE_TEACHERS;

            if (currentFragment.equals("SettingFragment")) {

                fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, teacherFragment).commit();

            } else {


                int lastIndex = fragmenList.indexOf(DataFragment.lastSelection);
                int newIndex = fragmenList.indexOf(selectionType);

                if (lastIndex > newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_down_start, R.anim.slide_down_exit).replace(R.id.content_frame, teacherFragment).commit();

                } else if (lastIndex < newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, teacherFragment).commit();

                } else {

                    fm.beginTransaction().replace(R.id.content_frame, teacherFragment).commit();
                }
            }

            this.setTitle(R.string.title_teachers);

        } else if (id == R.id.nav_buildings) {


            selectionType = DBHelper.TABLE_BUILDINGS;

            if (currentFragment.equals("SettingFragment")) {

                fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, buildingsFragment).commit();

            } else {

                int lastIndex = fragmenList.indexOf(DataFragment.lastSelection);
                int newIndex = fragmenList.indexOf(selectionType);

                if (lastIndex > newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_down_start, R.anim.slide_down_exit).replace(R.id.content_frame, buildingsFragment).commit();

                } else if (lastIndex < newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, buildingsFragment).commit();

                } else {

                    fm.beginTransaction().replace(R.id.content_frame, buildingsFragment).commit();

                }
            }

            this.setTitle(R.string.title_subject_type);

        } else if (id == R.id.nav_lessons_types) {


            selectionType = DBHelper.TABLE_TYPE;

            if (currentFragment.equals("SettingFragment")) {

                fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, typeFragment).commit();

            } else {

                int lastIndex = fragmenList.indexOf(DataFragment.lastSelection);
                int newIndex = fragmenList.indexOf(selectionType);

                if (lastIndex > newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_down_start, R.anim.slide_down_exit).replace(R.id.content_frame, typeFragment).commit();

                } else if (lastIndex < newIndex) {

                    fm.beginTransaction().setCustomAnimations(R.anim.slide_up_start, R.anim.slide_up_exit).replace(R.id.content_frame, typeFragment).commit();

                } else {

                    fm.beginTransaction().replace(R.id.content_frame, typeFragment).commit();

                }
            }

            this.setTitle(R.string.title_buildings);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {




            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }


}