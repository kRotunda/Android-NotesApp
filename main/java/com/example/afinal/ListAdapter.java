package com.example.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter{

    SQLiteHelper sqLiteHelper;

    public interface ListAdapterListener{
        public void getInfo(int position);
        public void deleteList(int position);
    }

    ListAdapterListener listAdapterListener;

    public ListAdapter(ListAdapterListener listAdapterListener, SQLiteHelper sqLiteHelper){
        this.listAdapterListener = listAdapterListener;
        this.sqLiteHelper = sqLiteHelper;
    }

    public class ListHolder extends RecyclerView.ViewHolder{
        TextView listName;


        public ListHolder(@NonNull View itemView) {
            super(itemView);

            listName = (TextView) itemView.findViewById(R.id.listNameText);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    listAdapterListener.deleteList(position);
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    listAdapterListener.getInfo(position);
                }
            });
        }

        public void updateTextViews(String name){
            listName.setText(name);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String name = sqLiteHelper.getName(position);
        ListHolder listHolder = (ListHolder) holder;
        listHolder.updateTextViews(name);
    }

    @Override
    public int getItemCount() {
        return sqLiteHelper.getCountList();
    }
}
