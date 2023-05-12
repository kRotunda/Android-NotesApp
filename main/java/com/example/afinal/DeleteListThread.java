package com.example.afinal;

import android.content.Context;

public class DeleteListThread extends Thread{

    SQLiteHelper sqLiteHelper;
    int id;

    public DeleteListThread(Context context, int id){
        this.sqLiteHelper = new SQLiteHelper(context);
        this.id = id;
    }

    public void run() {
        sqLiteHelper.deleteMainList(id);
    }
}
