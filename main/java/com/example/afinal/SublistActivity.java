package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class SublistActivity extends AppCompatActivity implements SublistAdapter.SublistAdapterListener{

    TextView descText;
    TextView nameText;
    SQLiteHelper sqLiteHelper;
    int intentPosition;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sublist);

        nameText = (TextView) findViewById(R.id.listNameOnSub);
        descText = (TextView) findViewById(R.id.listDescText);
        sqLiteHelper = new SQLiteHelper(this);

        Intent intent = getIntent();
        intentPosition = intent.getIntExtra("Position", 0);

        nameText.setText(sqLiteHelper.getName(intentPosition));
        descText.setText(sqLiteHelper.getDescription(intentPosition));

        recyclerView = findViewById(R.id.subListRecycler);
        recyclerView.setAdapter(new SublistAdapter(this, sqLiteHelper, intentPosition));
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
            Intent intent = new Intent(getApplicationContext(), CreateSublistActivity.class);
            intent.putExtra("Position", intentPosition);
            startActivity(intent);
        }
        return super .onOptionsItemSelected(item);
    }

    @Override
    public void getInfo(int position) {
        Intent intent = new Intent(getApplicationContext(), ItemViewActivity.class);
        intent.putExtra("Position", position);
        intent.putExtra("ListId", intentPosition);
        startActivity(intent);
    }

    @Override
    public void deleteList(int position) {
        Thread t1 = new DeleteSublistThread(getApplicationContext(), position, intentPosition);
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
        recyclerView.setAdapter(new SublistAdapter(this, sqLiteHelper, intentPosition));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}