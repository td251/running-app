package com.example.advancedmobile;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {
    private SensorManager sensorManager;
    private Sensor sensor;
    TextView welcomeMessage;
    String welcome = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("HELOP");
        if (getArguments() != null) {
            welcome = getArguments().getString("CURRENT_USER");
            System.out.println(this.welcome);
        }
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        Bundle bundle=getArguments();
        if(bundle != null)
        {
            welcomeMessage.setText("HELLO WORLD " + " " + welcome);

        }
        welcomeMessage = view.findViewById(R.id.textview_first);
        // Inflate the layout for this fragment
        return view;

    }
    public void setText(String text)
    {

    }
    public void displaySensors(){
        //        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}

