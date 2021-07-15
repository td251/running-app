package com.example.advancedmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.advancedmobile.Models.Activity;
import com.example.advancedmobile.Models.activityData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DataSource dataSource;
    ProgressDialog progressBar;
    TextView welcomeMessage;
    TextView noActivities;
    TextView YourActivities;
    CustomAdapter customAdapter;
    ArrayList<String> user_name, activity_time, activity_distance_travelled, activity_date, activty_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initalizing data source

        welcomeMessage = findViewById(R.id.textview_first);
        String userName = getIntent().getStringExtra("CURRENT_USER");
        super.onCreate(savedInstanceState);
        //chooses the layout to opeen
        setContentView(R.layout.activity_main);
        user_name = new ArrayList<>();
        activity_time = new ArrayList<>();
        activity_distance_travelled = new ArrayList<>();
        activity_date = new ArrayList<>();
        activty_type = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        dataSource = new DataSource(this);
        dataSource.open();
        storeDataInArray(userName);
        //list view passing arraylist
        customAdapter = new CustomAdapter(MainActivity.this, this, user_name, activity_time, activity_distance_travelled,
                activity_date, activty_type);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setSelectedItemId(R.id.home);
//        peform item selected listener
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.activity:
                        //start activity
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("CURRENT_USER", userName);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });
    }

    void storeDataInArray(String userName) {
        dataSource = new DataSource(this);

        dataSource.open();
        //retriving activities
        Cursor cursor = dataSource.getAllActivities(userName);
        if (cursor.getCount() == 0) {
            // if user with no activities display msg saying no activities
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            noActivities = findViewById(R.id.NoActivities);
            noActivities.setText("You have not yet created any activities press the activity button to get started!");
        } else {
            //populate list to view activities
            while (cursor.moveToNext()) {
                //populating array list
                user_name.add(cursor.getString(1));
                activity_time.add(cursor.getString(2));
                activity_distance_travelled.add(cursor.getString(3));
                activity_date.add(cursor.getString(4));
                activty_type.add(cursor.getString(5));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

