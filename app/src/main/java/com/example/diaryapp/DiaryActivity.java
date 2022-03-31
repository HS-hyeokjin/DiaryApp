package com.example.diaryapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DiaryActivity extends AppCompatActivity {

    private static final String TAG = "DiaryActivity";
    TextView dateText;
    EditText diary = null;
    Context context;
    DiaryListFragment diarylistFragment;
    MainActivity mainActivity;

    public static DiaryDatabase diaryDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        diarylistFragment = new DiaryListFragment();

        dateText = (TextView) findViewById(R.id.dateText);
        Intent intent = getIntent();
        String date = intent.getExtras().getString("selectDate");
        dateText.setText(date);
        diary = findViewById(R.id.writeDiary);

        openDatabase();
    }


    private void saveDiary() {
        String diaryContent = diary.getText().toString();
        Intent intent = getIntent();
        String date = intent.getExtras().getString("selectDate");
        String sqlSave = "insert into " + DiaryDatabase.TABLE_DIARY + " (diaryContent) values ( '" + date + "\n" + diaryContent + "')";

        DiaryDatabase database = DiaryDatabase.getInstance(context);
        database.execSQL(sqlSave);

        diary.setText("");
    }

    private void deleteDiary() {
        String diaryContent = diary.getText().toString();
        if (diaryContent != null) {
            diary.setText("");
        }
    }

    public void openDatabase() {
        // open database
        if (diaryDatabase != null) {
            diaryDatabase.close();
            diaryDatabase = null;
        }
        diaryDatabase = DiaryDatabase.getInstance(this);
        boolean isOpen = diaryDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    public void clickHandler(View v) {
        switch (v.getId()) {
            case R.id.save:
                saveDiary();
                break;
            case R.id.delete:
                deleteDiary();
                break;
            case R.id.load:
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
