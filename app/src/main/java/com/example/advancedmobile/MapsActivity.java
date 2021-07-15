package com.example.advancedmobile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.advancedmobile.DataSource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.advancedmobile.Models.Activity;
import com.example.advancedmobile.Models.LastPosition;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    private int seconds = 0;
    private DataSource dataSource;
    public static final int UPDATE_INTERVAL = 30;
    public static final int FASTEST_INTERVAL = 5;
    private static final String TAG = "Hello";
    private static final long INTERVAL = 1000 * 15 * 1; // 15 secs
    private static final long FASTEST = 1000 * 15 * 1; // 15 secs
    private static final float smallestDistance = 0.25f; //quarter of a metre
    public static String userName;

    private Activity activity;
    private ArrayList<LatLng> points;
    List<Address> addresses;
    Location currentLocation;
    LatLng startingPosition;
    TextView accuracy;

    private boolean wasRunning;
    private static final int REQUEST_CODE = 101;
    FusedLocationProviderClient fusedLocationProviderClient;
    ArrayList<Location> locationList;
    boolean isLogging;
    //if not selected just choose to select run
    String selectedActivity = "Run";
    private LocationRequest locationRequest;
    MarkerOptions markerOptions = new MarkerOptions();
    private boolean running;

    private int distanceTracker = 0;
    private int totalDistance = 0;
    private static String finalTime;
    private double kilometers;
    private Boolean addedSuccesfully;
    List<LastPosition> lastPositions = new ArrayList<LastPosition>();
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                //get coords
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //mark the starting positon
                markerOptions.position(latLng).title("Starting Point");
                //add to array
                points.add(latLng);
                drawPolyLine();
                //zoom
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 80));
                //add positon to array
                lastPositions.add(new LastPosition(location.getLatitude(), location.getLongitude()));
                distanceTracker++;
                //once array has two points
                if (distanceTracker > 1) {
                    distance(lastPositions.get(distanceTracker - 2).latitude, lastPositions.get(distanceTracker - 2).longitude, new LatLng(location.getLatitude(), location.getLongitude()));
                }

            }
        }
    };

    private void drawPolyLine() {
        //clearing all the markers and polylines
        myMap.clear();
        //details of poly line feature
        PolylineOptions lineOptions = new PolylineOptions().width(8).color(Color.BLUE).geodesic(true);
        //marking starting position
        markerOptions.position(points.get(0)).title("Starting position ");
        myMap.addMarker(markerOptions);
        for (int i = 0; i < points.size(); i++) {
            //iterate over points and add to array
            LatLng point = points.get(i);
            lineOptions.add(point);
        }
        //draw polyline
        myMap.addPolyline(lineOptions);

    }

    public double distance(double previousLat, double previousLon, LatLng newPosition) {
        //adding the positions to an array
        float[] results = new float[2];
        android.location.Location.distanceBetween(previousLat, previousLon, newPosition.latitude, newPosition.longitude, results);
        //finds the distance between two points and then puts the computed value into results


        if (results[0] != 0) {
            //accumlate result
            totalDistance += results[0];
        }
        //set view
        final TextView distanceView = findViewById(R.id.distance_view);
        distanceView.setText(String.valueOf(totalDistance));
        return results[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.userName = getIntent().getStringExtra("CURRENT_USER");
        super.onCreate(savedInstanceState);
        points = new ArrayList<LatLng>();
        setContentView(R.layout.activity_maps);
        accuracy = findViewById(R.id.textview_first);
        locationList = new ArrayList<>();
        isLogging = false;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(INTERVAL);
        //how fast application can handle request
        locationRequest.setFastestInterval(FASTEST);
        //set smallest amount of distance that needs to be traveled to start tracking
        locationRequest.setSmallestDisplacement(smallestDistance);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        showActivityTypeDialog();

        if (savedInstanceState != null) {

            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds
                    = savedInstanceState
                    .getInt("seconds");
            running
                    = savedInstanceState
                    .getBoolean("running");
            wasRunning
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setSelectedItemId(R.id.activity);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.activity:
                        //already on the page
                        return true;
                    case R.id.home:
                        //navigate to home
                        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                        intent.putExtra("CURRENT_USER", userName);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }
    //pause the running timer

    public void onPause(View view) {

        running = false;
        //saving an activity
        dataSource = new DataSource(this);
        dataSource.open();
        Date today = new Date();
        today.setHours(0);
        stopLocationUpdates();
        activity = new Activity();
        this.activity.distanceTravelled = this.totalDistance;
        this.activity.time = this.finalTime;
        this.activity.userName = this.userName;
        this.activity.dateCompleted = today.toString();
        this.activity.activityType = selectedActivity;
        this.activity.timeInSeconds = this.seconds;
        // speed = distance /time
        this.activity.speed = (this.activity.distanceTravelled / this.activity.timeInSeconds);
        //add to db
        this.addedSuccesfully = dataSource.createActivities(activity);
        //call congrats dialog
        buildDialog(activity);
        //reset the timer
        this.seconds = 0;
        //reset the distance
        this.totalDistance = 0;
    }

    public void buildDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        //alert message
        builder.setMessage("You have completed your, " + activity.activityType + "  cover a distance of " + activity.distanceTravelled + " meters in a average speed of " + activity.speed + " meters per second");
        builder.setTitle("Congratulations!");
        if (activity.activityType == "Run") {
            //setting the activity icon
            builder.setIcon(R.drawable.ic_runa);
        } else if (activity.activityType == "Walk") {
            builder.setIcon(R.drawable.ic_walk_foreground);
        } else if (activity.activityType == "Cycle") {
            builder.setIcon(R.drawable.ic_bike_foreground);
        }

        //set canceable force
        //when the user clicks somewehere else
        builder.setCancelable(false);
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //creating the alert dialog
        AlertDialog alertDialog = builder.create();
        // showing the alert dialog
        alertDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putInt("running", seconds);
        savedInstanceState.putInt("wasRunning", seconds);
    }

    @Override
    protected void onStart() {

        super.onStart();
        //checking for permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            //start location update
            checkSettingsAndStartLocationUpdates();


        } else {
            checkSettingsAndStartLocationUpdates();

        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //setting of devidce are satisfied
                // start location update
                startLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(MapsActivity.this, 1001);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }

    private void showActivityTypeDialog() {
        //activity type dialog box
        String[] activityType = {"Run", "Cycle", "Walk"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        //title
        builder.setTitle("Choose Activity");
        //selected option by default
        builder.setSingleChoiceItems(activityType, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedActivity = activityType[which];
                Toast.makeText(MapsActivity.this, "Your just clicked:" + selectedActivity, Toast.LENGTH_SHORT).show();

            }
        });
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //setting current location
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapsActivity.this);

                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        startingPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

//        myMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.Maps_style));
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = myMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.maps_style));

            if (!success) {
                Log.e("Map", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map", "Can't find style.", e);
        }

        myMap.getUiSettings().setZoomControlsEnabled(false);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
        myMap.animateCamera(CameraUpdateFactory.newLatLng(startingPosition));
        //zoom level
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startingPosition, 80));
    }

    public void onClickStart(View view) {
        running = true;
        runTimer();


    }

    private void runTimer() {
        final TextView timeView = findViewById(
                R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds % 36000) / 60;
                int secs = seconds % 60;
                //format seconds into hrs + mins + secs
                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d", hours,
                        minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
                setTime(time);
            }
        });
    }

    public void setTime(String timeValue) {
        this.finalTime = timeValue;

    }

    //if user hits the back button
    @Override
    public void onBackPressed() {
        if (running == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            //alert message
            builder.setMessage("Are you sure you wish to exit? Your activity won't be saved");
            builder.setTitle("Alert ! ");
            //set canceable force
            //when the user clicks somewehere else
            builder.setCancelable(false);
            builder.setPositiveButton(" Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            //creating the alert dialog
            AlertDialog alertDialog = builder.create();
            // showing the alert dialog
            alertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
//                    updateGPS();
                }
                break;
        }
    }

}