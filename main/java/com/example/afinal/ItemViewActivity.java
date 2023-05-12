package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

public class ItemViewActivity extends AppCompatActivity {

    int ItemPos, ListId;
    SQLiteHelper sqLiteHelper;
    TextView nameText, descText, dateText, timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        Intent intent = getIntent();
        ItemPos = intent.getIntExtra("Position", 0);
        ListId = intent.getIntExtra("ListId", 0);

        sqLiteHelper = new SQLiteHelper(this);

        nameText = (TextView) findViewById(R.id.ItemNameText);
        descText = (TextView) findViewById(R.id.ItemDescriptionText);
        dateText = (TextView) findViewById(R.id.ItemDueDateText);
        timeText = (TextView) findViewById(R.id.ItemDueTimeText);

        String[] arr = new String[7];
        arr = sqLiteHelper.getSubList(ItemPos, ListId+1);

        nameText.setText(arr[0]);
        descText.setText(arr[1]);
        if(Integer.parseInt(arr[2]) == -1){
            dateText.setText("Date Due:\nNone");
        } else {
            dateText.setText("Date Due:\n"+arr[2]+"/"+arr[3]+"/"+arr[4]);
        }
        if(Integer.parseInt(arr[5]) == -1){
            timeText.setText("Time Due:\nNone");
        }else{
            timeText.setText("Time Due:\n"+arr[5]+":"+arr[6]);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SublistActivity.class);
        intent.putExtra("Position", ItemPos);
        startActivity(intent);
    }

    public void completeItem(View view) {
        Thread t1 = new DeleteSublistThread(getApplicationContext(), ItemPos, ListId);
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ping);
        mp.start();

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(500);

        Intent intent = new Intent(getApplicationContext(), SublistActivity.class);
        intent.putExtra("Position", ListId);
        startActivity(intent);
    }
}