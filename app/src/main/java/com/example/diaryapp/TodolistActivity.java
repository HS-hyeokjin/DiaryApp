package com.example.diaryapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class TodolistActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Fragment todolistFragment;
    EditText inputToDo;
    Context context;
    TextView datetext;


    public static NoteDatabase noteDatabase = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        todolistFragment = new TodolistFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, todolistFragment).commit();

        datetext = (TextView) findViewById(R.id.datetext2);
        Intent intent = getIntent();
        String date = intent.getExtras().getString("selectDate");
        datetext.setText(date);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveToDo();
                Toast.makeText(getApplicationContext(),"추가되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        openDatabase();

        Button finishBtn = findViewById(R.id.savebtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveToDo(){
        inputToDo = findViewById(R.id.inputToDo);

        String todo = inputToDo.getText().toString();
        Intent intent = getIntent();
        String date = intent.getExtras().getString("selectDate");
        String sqlSave = "insert into " + NoteDatabase.TABLE_NOTE + " (TODO,DDATE) values (" +
                "'" + date+"ㅣ"+ todo + "','" + date +"')";

        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sqlSave);

        inputToDo.setText("");
    }
    public void openDatabase() {
        // open database
        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        noteDatabase = NoteDatabase.getInstance(this);
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

    }

}
