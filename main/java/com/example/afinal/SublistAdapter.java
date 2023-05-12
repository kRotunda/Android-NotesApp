package com.example.afinal;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SublistAdapter extends RecyclerView.Adapter{

    SQLiteHelper sqLiteHelper;
    int listPosition;

    public interface SublistAdapterListener{
        public void getInfo(int position);
        public void deleteList(int position);
    }

    SublistAdapter.SublistAdapterListener sublistAdapterListener;

    public SublistAdapter(SublistAdapter.SublistAdapterListener sublistAdapterListener, SQLiteHelper sqLiteHelper, int position){
        this.sublistAdapterListener = sublistAdapterListener;
        this.sqLiteHelper = sqLiteHelper;
        this.listPosition = position;
    }

    public class SublistHolder extends RecyclerView.ViewHolder{
        TextView listName, listDate, listTime;


        public SublistHolder(@NonNull View itemView) {
            super(itemView);

            listName = (TextView) itemView.findViewById(R.id.sublistName);
            listDate = (TextView) itemView.findViewById(R.id.dueDate);
            listTime = (TextView) itemView.findViewById(R.id.dueTime);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    sublistAdapterListener.deleteList(position);
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    sublistAdapterListener.getInfo(position);
                }
            });
        }

        public void updateTextViews(String name, int day, int month, int year, int hour, int minute){
            listName.setText(name);
            String date = new SimpleDateFormat("MMddyyyy", Locale.getDefault()).format(new Date());
            if (day != -1){

                if(Integer.parseInt(date) - ((month*1000000)+(day*10000)+year) < -50000){
                    listDate.setTextColor(Color.parseColor("#00FF00"));
                } else if(Integer.parseInt(date) - ((month*1000000)+(day*10000)+year) < 0){
                    listDate.setTextColor(Color.parseColor("#FFA500"));
                } else {
                    listDate.setTextColor(Color.parseColor("#FF0000"));
                }

                listDate.setText(month+"/"+day+"/"+year);
            }
            if (hour != -1){

                if(Integer.parseInt(date) - ((month*1000000)+(day*10000)+year) < -50000){
                    listTime.setTextColor(Color.parseColor("#00FF00"));
                } else if(Integer.parseInt(date) - ((month*1000000)+(day*10000)+year) < 0){
                    listTime.setTextColor(Color.parseColor("#FFA500"));
                } else {
                    listTime.setTextColor(Color.parseColor("#FF0000"));
                }

                listTime.setText(hour+":"+minute);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_list, parent, false);
        return new SublistAdapter.SublistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int id = sqLiteHelper.getId(listPosition);
        String[] arr = new String[7];
        arr = sqLiteHelper.getSubList(position, id);
        SublistAdapter.SublistHolder sublistHolder = (SublistAdapter.SublistHolder) holder;
        sublistHolder.updateTextViews(arr[0], Integer.parseInt(arr[3]), Integer.parseInt(arr[2]), Integer.parseInt(arr[4]), Integer.parseInt(arr[5]), Integer.parseInt(arr[6]));
    }

    @Override
    public int getItemCount() {
        return sqLiteHelper.getCountSublist(listPosition);
    }
}