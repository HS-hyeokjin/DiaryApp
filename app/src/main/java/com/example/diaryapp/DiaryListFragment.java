package com.example.diaryapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class DiaryListFragment extends Fragment {

    private static DiaryDatabase database;

    SwipeRefreshLayout swipeRefreshLayout;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    DiaryAdapter adapter;
    Context context;

    private static MainActivity mainActivity;
    private static DiaryActivity diaryActivity;

    public DiaryListFragment() {
        // Required empty public constructor
    }

    public static DiaryListFragment newInstance(String param1, String param2) {
        DiaryListFragment fragment = new DiaryListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_diraylist, container, false);
        initUI(rootView);
        loadDiaryListData();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDiaryListData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void initUI(ViewGroup rootView) {

        recyclerView = rootView.findViewById(R.id.recyclerView2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DiaryAdapter();
        recyclerView.setAdapter(adapter);

    }

    public int loadDiaryListData() {

        String loadSql = "select _id, diaryContent from " + DiaryDatabase.TABLE_DIARY + " order by _id desc";

        int recordCount = 0;
        DiaryDatabase database = DiaryDatabase.getInstance(context);

        if (database != null) {
            Cursor outCursor = database.rawQuery(loadSql);

            recordCount = outCursor.getCount();

            ArrayList<Diary> items = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String diaryContent = outCursor.getString(1);
                items.add(new Diary(_id, diaryContent));
            }
            outCursor.close();
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

        return recordCount;
    }

}
