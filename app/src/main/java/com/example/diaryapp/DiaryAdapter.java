package com.example.diaryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
    private static final String TAG = "DiaryAdapter";

    ArrayList<Diary> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.diary_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diary item = items.get(position);
        holder.setItem(item);
        holder.setLayout();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView textView;
        TextView textView2;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.diaryitem_layout);
            textView2 = itemView.findViewById(R.id.diaryitem_diarycontent2);
            deleteButton = itemView.findViewById(R.id.deleteButton2);
            deleteButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    String diary = (String) textView2.getText();
                    deleteDiary(diary);
                    Toast.makeText(v.getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }


                Context context;

                private void deleteDiary(String diary) {
                    String deleteSql = "delete from " + DiaryDatabase.TABLE_DIARY + " where " + "diaryContent = '" + diary + "'";
                    DiaryDatabase database = DiaryDatabase.getInstance(context);
                    database.execSQL(deleteSql);
                }
            });
        }

        public void setItem(Diary item) {
            textView2.setText(item.getDiaryContent());
        }

        public void setLayout() {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setItems(ArrayList<Diary> items) {
        this.items = items;
    }

}