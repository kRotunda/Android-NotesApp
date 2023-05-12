package com.example.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "todoList.sqlite";
    private static final int DB_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE List (_id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, description STRING);");
        sqLiteDatabase.execSQL("CREATE TABLE Sublist (_id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, description STRING, month INTEGER, day INTEGER, year INTEGER, hour INTEGER, minute INTEGER, list_id INTEGER, FOREIGN KEY(list_id) REFERENCES List(_id));");
    }

    void insertList(String name, String description){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        sqLiteDatabase.insert("List", null, contentValues);
    }

    void insertSublist(String name, String description, int month, int day, int year, int hour, int minute, int position){
        int id = getId(position);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("month", month);
        contentValues.put("day", day);
        contentValues.put("year", year);
        contentValues.put("hour", hour);
        contentValues.put("minute", minute);
        contentValues.put("list_id", id);
        sqLiteDatabase.insert("Sublist", null, contentValues);
    }

    int getCountList(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM List", null);
        return cursor.getCount();
    }

    int getCountSublist(int position){
        int id = getId(position);
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Sublist WHERE list_id = "+ id, null);
        return cursor.getCount();
    }

    public void deleteMainList(int position){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("List", "_id" + "=" + position, null);
    }

    public void deleteSubList(int position, int listId){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        int tempPos = 0;

        Cursor cursor = sqLiteDatabase.query("Sublist",
                new String[]{"_id", "list_id"},
                null,
                null,
                null,
                null,
                "list_id");

        while (true){
            if(cursor.moveToPosition(tempPos)){
                if (cursor.getInt(1) != (listId+1)){
                    tempPos++;
                } else {
                    tempPos += position;
                    break;
                }
            }
        }

        if(cursor.moveToPosition(tempPos)){
            sqLiteDatabase.delete("Sublist", "_id" + "=" + (cursor.getInt(0)), null);
        }
    }

    public String getName(int position) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("List",
                new String[]{"_id", "name"},
                null,
                null,
                null,
                null,
                "_id");
        if(cursor.moveToPosition(position)){
            String name = cursor.getString(1);
            return name;
        }
        return "";
    }

    public String[] getSubList(int position, int id) {
        int nextPosition = 0;
        String[] arr = new String[7];
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("Sublist",
                new String[]{"_id", "name", "description", "month", "day", "year", "hour", "minute", "list_id"},
                null,
                null,
                null,
                null,
                "name");
        while (true){
            if(cursor.moveToPosition(nextPosition)){
                int posId = cursor.getInt(8);
                if (posId != id) {
                    nextPosition += 1;
                }
                else {
                    if (position != 0){
                        nextPosition += 1;
                        position -= 1;
                        continue;
                    }
                    break;
                }
            }
        }
        if(cursor.moveToPosition(nextPosition)){
            String name = cursor.getString(1);
            arr[0] = name;
            String description = cursor.getString(2);
            arr[1] = description;
            int month = cursor.getInt(3);
            arr[2] = Integer.toString(month);
            int day = cursor.getInt(4);
            arr[3] = Integer.toString(day);
            int year = cursor.getInt(5);
            arr[4] = Integer.toString(year);
            int hour = cursor.getInt(6);
            arr[5] = Integer.toString(hour);
            int minute = cursor.getInt(7);
            arr[6] = Integer.toString(minute);
        }
        return arr;
    }

    public String getDescription(int position) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("List",
                new String[]{"_id", "description"},
                null,
                null,
                null,
                null,
                "description");
        if(cursor.moveToPosition(position)){
            String desc = cursor.getString(1);
            return desc;
        }
        return "";
    }

    public int getId(int position) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("List",
                new String[]{"_id", "_id"},
                null,
                null,
                null,
                null,
                "_id");
        if(cursor.moveToPosition(position)){
            int _id = cursor.getInt(1);
            return _id;
        }
        return -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}
