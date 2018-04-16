package com.mmstudio.timetable.Items;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmstudio.timetable.Animation.ZoomAnimation;
import com.mmstudio.timetable.Fragments.DataFragment;
import com.mmstudio.timetable.R;


public class SubjectItem {

    int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    public final static int NONE_ANIM_MODE =0;
    public final static int SHOW_ANIM_MODE =1;
    public final static int HIDE_ANIM_MODE =2;
    public final static int WITHOUT_ANIM_SHOW_MODE =3;


    private String[] data;
    int showMode;
    private Activity activity;

    public SubjectItem(Activity activity,String[] data,int showMode) {
        this.activity = activity;
        this.data = data;
        this.showMode = showMode;
    }

    public View getView(){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.subject_view,null);

        LinearLayout button_layout = v.findViewById(R.id.button_layout);

        if(showMode == SHOW_ANIM_MODE){
            button_layout.startAnimation(new ZoomAnimation(button_layout,0,WRAP_CONTENT,WRAP_CONTENT,WRAP_CONTENT));
            }else if(showMode == HIDE_ANIM_MODE){
            button_layout.startAnimation(new ZoomAnimation(button_layout,0,WRAP_CONTENT,WRAP_CONTENT,WRAP_CONTENT));
        }else if(showMode == NONE_ANIM_MODE){
            button_layout.getLayoutParams().width = WRAP_CONTENT;
        }else if(showMode == WITHOUT_ANIM_SHOW_MODE){

        }

        TextView subjectText = v.findViewById(R.id.subnameText);
        TextView roomText = v.findViewById(R.id.roomText);
        TextView corpusText = v.findViewById(R.id.corpusText);
        TextView typeText = v.findViewById(R.id.typeText);
        TextView teacherText = v.findViewById(R.id.teacherText);
        TextView sTimeText = v.findViewById(R.id.stimeText);
        TextView eTimeText = v.findViewById(R.id.etimeText);

        subjectText.setText(data[1]);
        roomText.setText(data[2]);
        corpusText.setText(data[5]);
        typeText.setText(data[4]);
        teacherText.setText(data[3]);

        if(activity.getPreferences(Context.MODE_PRIVATE).getBoolean("timeMode",false)){
            data[0]=DataFragment.to12hourFormat(data[0]);
        }

        sTimeText.setText(data[0].split("-")[0]);
        eTimeText.setText(data[0].split("-")[1]);







        return v;
    }

}
