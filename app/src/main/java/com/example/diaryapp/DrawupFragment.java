package com.example.diaryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class DrawupFragment extends Fragment {
    MainActivity mainActivity;

    public CalendarView calendar;
    public TextView diaryTextView;
    public View viewDateCheck;
    public Button toDoButton, diaryButton;
    CharSequence[] date = {null};


    public DrawupFragment() {
    }


    public static DrawupFragment newInstance(String param1, String param2) {
        DrawupFragment fragment = new DrawupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_drawup, container, false);

        calendar = rootView.findViewById(R.id.calendarView);
        diaryTextView = rootView.findViewById(R.id.diaryTextView);
        toDoButton = rootView.findViewById(R.id.toDoButton);
        diaryButton = rootView.findViewById(R.id.diaryButton);
        viewDateCheck = rootView.findViewById(R.id.view);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date[0] = year + "/" + (month + 1) + "/" + dayOfMonth;
                viewDateCheck.setVisibility(View.VISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                toDoButton.setVisibility(View.VISIBLE);
                diaryButton.setVisibility(View.VISIBLE);
                diaryTextView.setText(date[0]);
            }
        });

        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diary = new Intent(getActivity(), DiaryActivity.class);
                diary.putExtra("selectDate", date[0]);
                mainActivity.startActivity(diary);
            }
        });

        toDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent todolists = new Intent(getActivity(), TodolistActivity.class);
                todolists.putExtra("selectDate", date[0]);
                mainActivity.startActivity(todolists);
            }
        });
        return rootView;
    }
}