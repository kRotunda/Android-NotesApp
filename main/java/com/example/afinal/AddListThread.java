package com.example.afinal;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import java.util.Random;

public class AddListThread extends Thread{

    SQLiteHelper sqLiteHelper;
    String name, description;

    public AddListThread(Context context, String name, String description){
        this.sqLiteHelper = new SQLiteHelper(context);
        this.name = name;
        this.description = description;
    }

    public void run() {
        if(name.length() <= 0){
            name = "Unnamed List";
        }
        if(description.length() <= 0){
            description = "None";
        }
        sqLiteHelper.insertList(name, description);
    }
}
