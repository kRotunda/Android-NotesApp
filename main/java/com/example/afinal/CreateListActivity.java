package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateListActivity extends AppCompatActivity {

    EditText listName, listDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        listName = (EditText) findViewById(R.id.listNameEditText);
        listDescription = (EditText) findViewById(R.id.listDescriptionEditText);
    }

    public void createList(View view) {
        Thread t1 = new AddListThread(getApplicationContext(), listName.getText().toString(), listDescription.getText().toString());
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}