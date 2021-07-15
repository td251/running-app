package com.example.advancedmobile.Models;

import java.util.Date;

public class Activity {
    public int id;
    public String userName;
    public String time;
    public int timeInSeconds;
    public float speed;
    public String dateCompleted;
    public int distanceTravelled;
    public String activityType;

    //getters and setters for the Activity
    public int getId() {
        return id;
    }

    public void setId(int Id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(int distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

}
