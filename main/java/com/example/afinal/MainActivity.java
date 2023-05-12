package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ListAdapter.ListAdapterListener{

    RecyclerView recyclerView;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteHelper = new SQLiteHelper(this);
        recyclerView = findViewById(R.id.toDoList);

        recyclerView.setAdapter(new ListAdapter(this, sqLiteHelper));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.addList){
            Intent intent = new Intent(getApplicationContext(), CreateListActivity.class);
            startActivity(intent);
        }
        return super .onOptionsItemSelected(item);
    }

    @Override
    public void getInfo(int position) {
        Intent intent = new Intent(getApplicationContext(), SublistActivity.class);
        intent.putExtra("Position", position);
        startActivity(intent);
    }

    @Override
    public void deleteList(int position) {
        Thread t1 = new DeleteListThread(getApplicationContext(), sqLiteHelper.getId(position));
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
        recyclerView.setAdapter(new ListAdapter(this, sqLiteHelper));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}