package com.mmstudio.timetable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class DataFragment extends Fragment {
    private String selectionType;
    private ArrayList<String> dataList =new ArrayList<>();
    FloatingActionButton fab;
    ArrayAdapter<String> adapter;

    @SuppressLint("ValidFragment")
    public DataFragment(String selectionType) {
        this.selectionType = selectionType;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_fragment, container, false);

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);









        SwipeMenuListView listView = v.findViewById(R.id.list_of_data);

        dataList = readFromDB(getActivity(),selectionType);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {



                // create "delete" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
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

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // edit
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final EditText editText = new EditText(getActivity());
                        editText.setText(dataList.get(position));
                        builder.setTitle(R.string.edit_data).
                                setView(editText).
                                setCancelable(false)
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newData = String.valueOf(editText.getText());
                                replaceDataToDB(getActivity(),selectionType,dataList.get(position),newData);
                                dataList.set(position,newData);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                        break;
                    case 1:
                        // delete

                        Toast.makeText(getActivity(), dataList.get(position) + " "+getResources().getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                        DBHelper dbHelper = new DBHelper(getActivity());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(selectionType,DBHelper.KEY_VALUE+"=?",new String[]{dataList.get(position)});
                        dataList.remove(position);
                        adapter.notifyDataSetChanged();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        fab = v.findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final EditText editText = new EditText(getActivity());
                builder.setTitle(R.string.add_data_item_title).
                        setView(editText).
                        setCancelable(false)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newData = String.valueOf(editText.getText());
                        addDataToDB(getActivity(),selectionType,newData);
                        dataList.add(newData);
                        adapter.notifyDataSetChanged();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return v;
    }

    public static ArrayList<String> readFromDB(Activity activity, String table) {
        DBHelper dbHelper = new DBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = db.query(table, null, null, null, null, null, DBHelper.KEY_VALUE + " ASC", null);

        if(cursor.getCount() !=0){
            while (cursor.moveToNext()){
                list.add(cursor.getString(1));
            }
        }else{
            Toast.makeText(activity,R.string.empty_data,Toast.LENGTH_SHORT).show();
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
