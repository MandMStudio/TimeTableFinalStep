package com.mmstudio.timetable.Items;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmstudio.timetable.Animation.ZoomAnimation;
import com.mmstudio.timetable.DBHelper;
import com.mmstudio.timetable.Fragments.DataFragment;
import com.mmstudio.timetable.R;

import static com.mmstudio.timetable.Items.SubjectItem.HIDE_ANIM_MODE;
import static com.mmstudio.timetable.Items.SubjectItem.NONE_ANIM_MODE;
import static com.mmstudio.timetable.Items.SubjectItem.WITHOUT_ANIM_SHOW_MODE;
import static com.mmstudio.timetable.Items.SubjectItem.SHOW_ANIM_MODE;

public class DayItem {
    private String day;
    private int week;
    private int showMode;
    private String[][] data;
    private Activity activity;
    public static int DONT_SHOW_MODE =5;
    public DayItem( Activity activity,String day,int week, String[][] data,  int showMode) {
        this.day = day;
        this.showMode = showMode;
        this.data = data;
        this.activity = activity;
        this.week = week;
    }


    public View getView(){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.day_view,null);

        TextView dayNameText = v.findViewById(R.id.dayName);
        dayNameText.setText(getNormalizeDayName(day));

        ImageButton add_button = v.findViewById(R.id.add_butt);
        if(showMode == SHOW_ANIM_MODE){
            add_button.startAnimation(new ZoomAnimation(add_button,50,0,50,50));
        }else if(showMode == HIDE_ANIM_MODE){
            add_button.startAnimation(new ZoomAnimation(add_button,50,50,50,0));
        }else if(showMode == NONE_ANIM_MODE){

        }else if(showMode == WITHOUT_ANIM_SHOW_MODE){
            add_button.getLayoutParams().height = 50;
        }else if(showMode == DONT_SHOW_MODE){
            dayNameText.getLayoutParams().height = 0;
            return v;
        }




        LinearLayout content = v.findViewById(R.id.content);

        for(int i=0;i< DataFragment.readDayFromDB(activity,(day+Integer.toString(week))).length;i++){
            String[] subjectData = data[i];
            content.addView(new SubjectItem(activity,data[i],showMode).getView());

        }





        return v;
    }

    private String getNormalizeDayName(String rowString){
        String res="";

        switch (rowString){
            case DBHelper.TABLE_MON:
                res =  activity.getResources().getString(R.string.monday);
                break;
            case DBHelper.TABLE_TUE:
                res =  activity.getResources().getString(R.string.tuesday);
                break;
            case DBHelper.TABLE_WEN:
                res =  activity.getResources().getString(R.string.wednesday);
                break;
            case DBHelper.TABLE_THU:
                res = activity.getResources().getString(R.string.thursday);
                break;
            case DBHelper.TABLE_FRI:
                res =  activity.getResources().getString(R.string.friday);
                break;
            case DBHelper.TABLE_SAT:
                res =  activity.getResources().getString(R.string.saturday);
                break;
            case DBHelper.TABLE_SUN:
                res =  activity.getResources().getString(R.string.sunday);
                break;


        }



        return res;
    }
}
