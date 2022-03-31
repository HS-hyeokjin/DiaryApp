package com.example.diaryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class TodolistListFragment extends Fragment {
    private static final String TAG = "MainFragment";


    SwipeRefreshLayout swipeRefreshLayout;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    NoteAdapter adapter;
    Context context;


    public TodolistListFragment() {
    }

    public static TodolistListFragment newInstance(String param1, String param2) {
        TodolistListFragment fragment = new TodolistListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_todolistlist, container, false);

        initUI(rootView);

        loadNoteListData();


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNoteListData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;

    }

    private void initUI(ViewGroup rootView) {

        recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //어댑터 연결결
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

    }

    @SuppressLint("Range")
    public int loadNoteListData() {


        String loadSql = "select _id, TODO, DDATE from " + NoteDatabase.TABLE_NOTE + " order by DDATE desc, _id desc";

        int recordCount = -1;
        NoteDatabase database = NoteDatabase.getInstance(context);

        if (database != null) {
            Cursor outCursor1 = database.rawQuery(loadSql);

            if (outCursor1 != null) {
                recordCount = outCursor1.getCount();
            }
            ArrayList<Note> items = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                outCursor1.moveToNext();

                int _id = outCursor1.getInt(0);
                String todo = outCursor1.getString(1);
                String date = outCursor1.getString(1);

                items.add(new Note(_id, todo, date));
            }
            outCursor1.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

        return recordCount;
    }
}