package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateSublistActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    int setYear = -1;
    int setDay = -1;
    int setMonth = -1;
    int setHour = -1;
    int setMinute = -1;
    int position;

    TextView dateText, timeText, sublistNameText, sublistDescText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sublist);

        dateText = (TextView) findViewById(R.id.dueDateText);
        timeText = (TextView) findViewById(R.id.dueTimeText);
        sublistNameText = (TextView) findViewById(R.id.subListNameText);
        sublistDescText = (TextView) findViewById(R.id.sublsitDescrptitionText);

        Intent intent = getIntent();
        position = intent.getIntExtra("Position", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.set_date_time_menue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.setDate){
            DateFragment dateFragment = new DateFragment(this);
            dateFragment.show(getSupportFragmentManager(), null);
        }
        if(id == R.id.setTime){
            TimeFragment timeFragment = new TimeFragment(this);
            timeFragment.show(getSupportFragmentManager(), null);
        }
        return super .onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day){
        setDay = day;
        setYear = year;
        setMonth = month+1;

        dateText.setText("Date Due: "+setMonth+"/"+setDay+"/"+setYear);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute){
        setHour = hour;
        setMinute = minute;
        if (setMinute < 10){
            timeText.setText("Time Due: "+setHour+":0"+setMinute);
        } else {
            timeText.setText("Time Due: "+setHour+":"+setMinute);
        }
    }

    public void createSublist(View view) {
        String name = sublistNameText.getText().toString();
        String description = sublistDescText.getText().toString();

        Thread t1 = new AddSublistThread(getApplicationContext(), name, description, setMonth, setDay, setYear, setHour, setMinute, position);
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), SublistActivity.class);
        intent.putExtra("Position", position);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SublistActivity.class);
        intent.putExtra("Position", position);
        startActivity(intent);
    }
}