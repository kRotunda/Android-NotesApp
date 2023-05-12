package com.example.afinal;

import android.content.Context;
import android.util.Log;

public class DeleteSublistThread extends Thread{
    SQLiteHelper sqLiteHelper;
    int position, listId;

    public DeleteSublistThread(Context context, int position, int listId){
        this.sqLiteHelper = new SQLiteHelper(context);
        this.position = position;
        this.listId = listId;
    }

    public void run() {
        sqLiteHelper.deleteSubList(position, listId);
    }
}
