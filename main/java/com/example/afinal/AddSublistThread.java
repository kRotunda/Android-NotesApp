package com.example.afinal;

import android.content.Context;

public class AddSublistThread extends Thread{
    SQLiteHelper sqLiteHelper;
    String name, description;
    int month, day, year, hour, minute, position;

    public AddSublistThread(Context context, String name, String description, int month, int day, int year, int hour, int minute, int position){
        this.sqLiteHelper = new SQLiteHelper(context);
        this.name = name;
        this.description = description;
        this.month = month;
        this.day = day;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.position = position;
    }

    public void run() {
        if(name.length() <= 0){
            name = "Unnamed Item";
        }
        if(description.length() <= 0){
            description = "None";
        }
        sqLiteHelper.insertSublist(name, description, month, day, year, hour, minute, position);
    }
}
