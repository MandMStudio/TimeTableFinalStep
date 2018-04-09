package com.mmstudio.timetable.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.mmstudio.timetable.DBHelper;
import com.mmstudio.timetable.MainActivity;
import com.mmstudio.timetable.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressLint("ValidFragment")
public class DataFragment extends Fragment {
    public static String lastSelection;
    private String selectionType;
    private List<String> dataList =new ArrayList<>();
    private FloatingActionButton fab;
    private ArrayAdapter<String> adapter;

    @SuppressLint("ValidFragment")
    public DataFragment(String selectionType) {
        this.selectionType = selectionType;
        lastSelection = selectionType;
    }

    public DataFragment() {
        selectionType = lastSelection;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_fragment, container, false);
        MainActivity.currentFragment = "DataFragment";
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);




        dataList = readFromDB(getActivity(),selectionType); //read from DB data and add it to dataList






        SwipeMenuListView listView = v.findViewById(R.id.list_of_data); //Create ListView with slide buttons


        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, dataList);//Just simple adapter. You can change view if one item by replacing second parameter to your

        listView.setAdapter(adapter);//accepting adapter to listView


        //Customization of slides buttons
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {



                // create "delete" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0x00,
                        0x00, 0xFF)));
                // set item width
                editItem.setWidth(170);
                // set a icon
                editItem.setIcon(R.drawable.edit);

                // add to menu
                menu.addMenuItem(editItem);



                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.delete);

                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);


        //Listener of slides buttons
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //for working back button


                        // edit button
                        //edit for time
                        if(selectionType.equals(DBHelper.TABLE_TIME)){
                            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_left_enter,R.anim.slide_left_exit).replace(R.id.content_frame, new TimeExpandFragment(dataList.get(position))).commit();
                        }else {

                            //edit window for simple data
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            //edit textField
                            final EditText editText = new EditText(getActivity());
                            //Focus keyboard on TextField
                            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    editText.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                                        }
                                    });
                                }
                            });
                            editText.requestFocus();
                            //Set keyboard type where it have done button
                            editText.setInputType(InputType.TYPE_CLASS_TEXT);


                            //set text of text field to oldValue
                            editText.setText(dataList.get(position));
                            //setting of some customizing
                            builder.setTitle(R.string.edit_data).
                                    setView(editText).
                                    setCancelable(true)
                                    //cancel button actions
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();

                                        }
                                    })
                                    //accepting change button actions
                                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            String newData = String.valueOf(editText.getText());//get new value of Text field

                                            replaceDataToDB(getActivity(), selectionType, dataList.get(position), newData);//replacing data in DB

                                            dataList.set(position, newData);//replacing data in dataList(local storage)

                                            adapter.notifyDataSetChanged();//refreshing adapter to confirm changes
                                        }
                                    });


                            AlertDialog alert = builder.create();
                            alert.show();

                        }

                        break;
                    case 1:
                        // delete button

                        //connecting to DB
                        DBHelper dbHelper = new DBHelper(getActivity());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        //delete from db
                        db.delete(selectionType,DBHelper.KEY_VALUE+"=?",new String[]{dataList.get(position)});

                        dataList.remove(position);//removing from local storage
                        adapter.notifyDataSetChanged();//refreshing adapter
                        //notify user of deleting item
                        Toast.makeText(getActivity(), dataList.get(position) + " "+getResources().getString(R.string.deleted),Toast.LENGTH_SHORT).show();


                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        //Initialization of FloatingActionButton
        fab = v.findViewById(R.id.fab);
        fab.bringToFront();
        //if its not data for times than we crate simple add window
        if(!selectionType.equals(DBHelper.TABLE_TIME)) {//if its not data for Time then we create simple adding window
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //for working back button


                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//Buider of adding window
                    final EditText editText = new EditText(getActivity());//text field where we go write user data


                    //Focus keyboard on TextField
                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            editText.post(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                        }
                    });
                    editText.requestFocus();

                    //Set keyboard type where it have done button
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);

                    //starting customize our window
                    builder.setTitle(R.string.add_data_item_title).
                            setView(editText).
                            setCancelable(true)
                            //cancel button actions
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            //add button actions
                            .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String newData = String.valueOf(editText.getText());//data from text field

                            if(!newData.isEmpty()){ //if text field is not empty than we can add it to database and our local storage
                                addDataToDB(getActivity(), selectionType, newData);//adding new data to db
                                dataList.add(newData);//adding data to local storage
                                adapter.notifyDataSetChanged();//refreshing adapter to show changes


                            }else{
                                //show some text if text field is empty
                                Toast.makeText(getActivity(),R.string.empty_data,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //building window
                    final AlertDialog alert = builder.create();
                    alert.show();

                    //customizing our done button on keyboard
                    editText.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if(event.getAction() == KeyEvent.ACTION_DOWN){//press down
                                    if(keyCode == KeyEvent.KEYCODE_ENTER){//done button

                                        //Next is the same like in add button on simple add window
                                        String newData = String.valueOf(editText.getText());

                                        if(!newData.isEmpty()){
                                            addDataToDB(getActivity(), selectionType, newData);
                                            dataList.add(newData);
                                            adapter.notifyDataSetChanged();
                                            editText.clearFocus();
                                            alert.cancel();

                                        }else{
                                            Toast.makeText(getActivity(),R.string.empty_data,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            }
                            return false;
                        }
                    });

                }
            });
            //Its fab click for time data
        }else{
            fab.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    //go to expanded fragment with time pickers
                    //and do some animation of transactions
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_left_enter,R.anim.slide_left_exit).replace(R.id.content_frame, new TimeExpandFragment()).commit();

                }
            });
        }


        return v;
    }

    public static ArrayList<String> readFromDB(Activity activity, String table) {
        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = db.query(table, null, null, null, null, null, null, null);

        if(cursor.getCount() !=0){
            while (cursor.moveToNext()){
                list.add(cursor.getString(1));
            }
        }else{
            Toast.makeText(activity,R.string.empty_list_of_data,Toast.LENGTH_SHORT).show();
        }

        cursor.close();

        return list;
    }
    public static ArrayList<String> readSettingsFromDB(Activity activity) {
        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = db.query(DBHelper.TABLE_SETTINGS, null, null, null, null, null, null, null);

        if(cursor.getCount() !=0){
            while (cursor.moveToNext()){
                list.add(cursor.getString(2));
            }
        }else{

        }

        cursor.close();

        return list;
    }
    public static void addDataToDB(Activity activity,String table, String data){

        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_VALUE, data);
        db.insert(table, null, cv);



    }
    public static void replaceDataToDB(Activity activity,String table, String oldData,String newData){

        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_VALUE, newData);
        db.update(table,cv,DBHelper.KEY_VALUE+"=?",new String[]{oldData});



    }

}
