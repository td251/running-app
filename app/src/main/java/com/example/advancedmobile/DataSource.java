package com.example.advancedmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.advancedmobile.Models.Activity;
import com.example.advancedmobile.Models.Login;
import com.example.advancedmobile.Models.Users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private DBHelper dbHelper;
    private SQLiteDatabase akushonDatabase;
    private String[] allColumns = {dbHelper.COLUMN_ID, dbHelper.COLUMN_USER_FIRSTNAME, DBHelper.COLUMN_USERNAME, DBHelper.COLUMN_USER_PASSWORD};
    private Users users;
    public static final String TABLE_USER = "User";
    public static final String TABLE_ACTIVITY = "Activity";
    private static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_FIRSTNAME = "user_first_name ";
    public static final String COLUMN_USERNAME = "user_name ";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_ACTIVITY_TIME = "activity_time";
    public static final String COLUMN_ACTIVITY_DATE = "activity_date";
    public static final String COLUMN_ACTIVITY_DISTANCETRAVELLED = "activity_distance_travelled";
    public static final String COLUMN_ACTIVITY_TYPE = "activity_type";

    private static final String DATABASE_NAME = "Akushon.db";
    public static final String COLUMN_DATA = "value";
    public static final String COLUMN_ID = "id";
    private static final int DATABASE_VERSION = 2;
    public static final String USER_TABLE_CREATE =
            "create table "
                    + TABLE_USER + "("
                    + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT,"
                    + COLUMN_USER_FIRSTNAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";
    public static final String ACTIVITY_TABLE_CREATE = "create table" +
            TABLE_ACTIVITY + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT,"
            + COLUMN_ACTIVITY_TIME + "TEXT," + COLUMN_ACTIVITY_DISTANCETRAVELLED + "TEXT," + COLUMN_ACTIVITY_DATE + "TEXT," + ")";

    DataSource(Context context) {
        dbHelper = new DBHelper(context);
    }


    //thros exception means if
    public void open() throws SQLException {
        akushonDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean createUser(Users user) {
        //assigning values
        ContentValues vals = new ContentValues();
        vals.put(dbHelper.COLUMN_USER_FIRSTNAME, user.name);
        vals.put(dbHelper.COLUMN_USERNAME, user.userName);
        vals.put(dbHelper.COLUMN_USER_PASSWORD, user.password);
        long rowInserted = akushonDatabase.insert(DBHelper.TABLE_USER, null, vals);
        if (rowInserted == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean createActivities(Activity activity) {

        //creating activities
        ContentValues vals = new ContentValues();
        vals.put(dbHelper.COLUMN_USERNAME, activity.userName);
        vals.put(dbHelper.COLUMN_ACTIVITY_TIME, activity.time);
        vals.put(dbHelper.COLUMN_ACTIVITY_DISTANCETRAVELLED, activity.distanceTravelled);
        vals.put(dbHelper.COLUMN_ACTIVITY_DATE, String.valueOf(activity.dateCompleted));
        vals.put(dbHelper.COLUMN_ACTIVITY_TYPE, activity.activityType);
        vals.put(dbHelper.COLUMN_ACTIVITY_TIME_IN_SECONDS, activity.timeInSeconds);
        vals.put(dbHelper.COLUMN_ACTIVITY_SPEED, activity.speed);
        long rowInserted = akushonDatabase.insert(DBHelper.TABLE_ACTIVITY, null, vals);
        //if unsucessful
        if (rowInserted == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkUserExists(Login login) {
        String[] columns = {"user_name"};
        //selection from db
        String selection = "user_name=? and user_password=?";
        String[] selectionArgs = {login.userName, login.password};
        Cursor cursor = akushonDatabase.query(DBHelper.TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        close();
        //if match username and passwords
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    //retrieve all activities
    public Cursor getAllActivities(String userName) {

        List<Activity> listOfActivities = new ArrayList<>();
        //selecting so just need to read the databse
        //if writeable database is locked
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE user_name = '" + userName + "';";
        Cursor cursor = db.rawQuery(queryString, null);
        Activity activity = new Activity();
        return cursor;
    }

}
