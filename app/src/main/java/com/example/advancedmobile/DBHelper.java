package com.example.advancedmobile;
import com.example.advancedmobile.Models.Users;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_USER = "User";
    public static final String TABLE_ACTIVITY = "Activity";
    private static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_FIRSTNAME = "user_first_name ";
    public static final String COLUMN_USERNAME = "user_name ";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_ACTIVITY_TIME  = "activity_time";
    public static final String COLUMN_ACTIVITY_DATE = "activity_date";
    public static final String COLUMN_ACTIVITY_DISTANCETRAVELLED = "activity_distance_travelled";
    public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    public static final String COLUMN_ACTIVITY_TIME_IN_SECONDS = "activity_time_in_seconds";
    public static final String COLUMN_ACTIVITY_SPEED = "activity_speed";
    private static final String DATABASE_NAME = "Akushon.db";
    public static final String COLUMN_DATA = "value";
    public static final String COLUMN_ID = "id";
    private static final int DATABASE_VERSION = 1;
    //sql creatiion query
    public static final String USER_TABLE_CREATE  =
            "create table "

                    + TABLE_USER + "("
                    + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT,"
                    + COLUMN_USER_FIRSTNAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    public static final String ACTIVITY_TABLE_CREATE = "create table" +
            TABLE_ACTIVITY + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT,"
            + COLUMN_ACTIVITY_TIME + " TEXT," + COLUMN_ACTIVITY_DISTANCETRAVELLED + " TEXT," + COLUMN_ACTIVITY_DATE + " TEXT," +COLUMN_ACTIVITY_TYPE + " TEXT,"+  " TEXT," +COLUMN_ACTIVITY_TIME_IN_SECONDS + " TEXT," + COLUMN_ACTIVITY_SPEED +  ")";
    //delete the user table
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 3);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating the user table
        db.execSQL(  "create table "

                + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT,"
                + COLUMN_USER_FIRSTNAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")" );
        //creating the activity table
        db.execSQL( "create table "

                +
                TABLE_ACTIVITY + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT,"
                + COLUMN_ACTIVITY_TIME + " TEXT," + COLUMN_ACTIVITY_DISTANCETRAVELLED + " TEXT," + COLUMN_ACTIVITY_DATE + " TEXT," + COLUMN_ACTIVITY_TYPE + " TEXT,"+ COLUMN_ACTIVITY_TIME_IN_SECONDS + " TEXT," + COLUMN_ACTIVITY_SPEED + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY );
        onCreate(db);
    }




}