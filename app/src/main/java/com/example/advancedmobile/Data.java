package com.example.advancedmobile;

public class Data {
    private long id;
    private String data;

    public Data() {
    }

    public Data(String newData) {
        data = newData;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return data;
    }
}
