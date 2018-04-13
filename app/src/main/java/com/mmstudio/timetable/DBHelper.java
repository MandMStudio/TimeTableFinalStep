package com.mmstudio.timetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DBTimeTable";
    public static final String TABLE_TIME = "time";
    public static final String TABLE_SUB = "subjects";
    public static final String TABLE_ROOM = "room";
    public static final String TABLE_TEACHERS = "teachers";
    public static final String TABLE_BUILDINGS = "buildings";
    public static final String TABLE_TYPE = "lesson_type";

    public static final String TABLE_SETTINGS = "settings";

    public static final String TABLE_MON = "monday";
    public static final String TABLE_TUE = "tuesday";
    public static final String TABLE_WEN = "wednesday";
    public static final String TABLE_THU = "thursday";
    public static final String TABLE_FRI = "friday";
    public static final String TABLE_SAT = "saturday";
    public static final String TABLE_SUN = "sunday";



    public static final String KEY_ID = "_id";
    public static final String KEY_VALUE = "value";
    public static final String KEY_SETTING_NAME = "settingName";




    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tables for data

        db.execSQL("create table  " + TABLE_TIME      + " (" + KEY_ID + " integer primary key," + KEY_VALUE + " text)");
        db.execSQL("create table  " + TABLE_SUB       + " (" + KEY_ID + " integer primary key," + KEY_VALUE + " text)");
        db.execSQL("create table  " + TABLE_ROOM      + " (" + KEY_ID + " integer primary key," + KEY_VALUE + " text)");
        db.execSQL("create table  " + TABLE_TEACHERS  + " (" + KEY_ID + " integer primary key," + KEY_VALUE + " text)");
        db.execSQL("create table  " + TABLE_TYPE      + " (" + KEY_ID + " integer primary key," + KEY_VALUE + " text)");
        db.execSQL("create table  " + TABLE_BUILDINGS + " (" + KEY_ID + " integer primary key," + KEY_VALUE + " text)");

        //Settings table with data
        db.execSQL("create table  " + TABLE_SETTINGS + " (" + KEY_ID + " integer primary key," + KEY_SETTING_NAME + " text," + KEY_VALUE + " text)");
        db.execSQL("INSERT INTO "+ TABLE_SETTINGS + "("+KEY_SETTING_NAME+", "+KEY_VALUE+") VALUES ('timeFormat','24mode')");
        db.execSQL("INSERT INTO "+ TABLE_SETTINGS + "("+KEY_SETTING_NAME+", "+KEY_VALUE+") VALUES ('lessonLenth','45')");
        db.execSQL("INSERT INTO "+ TABLE_SETTINGS + "("+KEY_SETTING_NAME+", "+KEY_VALUE+") VALUES ('firstTimePick','yes')");
        db.execSQL("INSERT INTO "+ TABLE_SETTINGS + "("+KEY_SETTING_NAME+", "+KEY_VALUE+") VALUES ('WeeksMode','oneWeekMode')");
        db.execSQL("INSERT INTO "+ TABLE_SETTINGS + "("+KEY_SETTING_NAME+", "+KEY_VALUE+") VALUES ('firstMondayDate','none')");

        //First Week
        db.execSQL("create table  " + TABLE_MON + "1 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_TUE + "1 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_WEN + "1 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_THU + "1 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_FRI + "1 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_SAT + "1 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_SUN + "1 (time text,subject text, room text, teacher text,type text, building text)");

        //Second Week
        db.execSQL("create table  " + TABLE_MON + "2 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_TUE + "2 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_WEN + "2 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_THU + "2 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_FRI + "2 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_SAT + "2 (time text,subject text, room text, teacher text,type text, building text)");
        db.execSQL("create table  " + TABLE_SUN + "2 (time text,subject text, room text, teacher text,type text, building text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Table for data
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_TIME);
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_SUB);
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_ROOM);
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_TEACHERS);
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_BUILDINGS);
        //FirstWeek
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_SUB+"1");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_MON+"1");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_TUE+"1");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_WEN+"1");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_THU+"1");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_FRI+"1");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_SAT+"1");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_SUN+"1");
        //SecondWeek
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_SUB+"2");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_MON+"2");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_TUE+"2");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_WEN+"2");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_THU+"2");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_FRI+"2");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_SAT+"2");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_SUN+"2");

        onCreate(db);

    }



}