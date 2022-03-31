package com.example.diaryapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiaryDatabase {
    private static final String TAG = "DiaryDatabase";

    private static DiaryDatabase database;
    public static String DATABASE_NAME = "diary.db";
    public static String TABLE_DIARY = "DIARY";
    public static int DATABASE_VERSION = 2;

    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;


    private DiaryDatabase(Context context) {
        this.context = context;
    }

    public static DiaryDatabase getInstance(Context context) {
        if (database == null) {
            database = new DiaryDatabase(context);
        }

        return database;
    }

    public Cursor rawQuery(String SQL) {

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in rawQuery", ex);
        }

        return c1;
    }

    //sql문을 실행시키는 역할을 함
    public boolean execSQL(String SQL) {

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in execSQL", ex);
            return false;
        }
        return true;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            //테이블초기화
            String DROP_SQL = "drop table if exists " + TABLE_DIARY;
            try {
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table " + TABLE_DIARY + "("
                    + " _id integer NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  diaryContent TEXT DEFAULT '', "
                    + "  dateTextContent TEXT DEFAULT '' "
                    + ")";
            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            //테이블의 인덱스 붙이기
            String CREATE_INDEX_SQL = "create index " + TABLE_DIARY + "_IDX ON " + TABLE_DIARY + "("
                    + "_id"
                    + ")";
            try {
                db.execSQL(CREATE_INDEX_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_INDEX_SQL", ex);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARY);
            onCreate(db);
        }
    }

    public boolean open() {

        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close() {
        db.close();
        database = null;
    }


}