package com.example.advancedmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private ArrayList user_name, activity_time, activity_distance_travelled, activity_date, activity_type;
    private Context context;

    CustomAdapter(MainActivity mainActivity, Context context, ArrayList user_name, ArrayList activity_time, ArrayList activity_distance_travelled, ArrayList activity_date, ArrayList activity_type) {
        this.context = context;
        this.user_name = user_name;
        this.activity_time = activity_time;
        this.activity_distance_travelled = activity_distance_travelled;
        this.activity_date = activity_date;
        this.activity_type = activity_type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //inflate fragment
        View view = inflater.inflate(R.layout.my_rpw, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //setting text of values
        holder.user_name_txt.setText(String.valueOf(activity_type.get(position)));
        holder.activity_time_txt.setText(String.valueOf(activity_time.get(position)));
        holder.activity_distance_travelled_txt.setText(String.valueOf(activity_distance_travelled.get(position)) + " Meters");
        String dateText = String.valueOf(activity_date.get(position));
        String finalView = dateText.substring(0, 10);
        holder.activity_date_txt.setText(finalView);

    }

    @Override
    public int getItemCount() {
        return user_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_name_txt, activity_time_txt, activity_distance_travelled_txt, activity_date_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //finding the text edits in the front end
            user_name_txt = itemView.findViewById(R.id.user_name_txt);
            activity_time_txt = itemView.findViewById(R.id.activity_time_txt);
            activity_distance_travelled_txt = itemView.findViewById(R.id.activity_distance_travelled_txt);
            activity_date_txt = itemView.findViewById(R.id.activity_date_txt);

        }
    }
}
