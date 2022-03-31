package com.example.diaryapp;

public class Note {
    int _id;
    String todo;
    String date;

    public Note(int _id, String todo, String date) {
        this._id = _id;
        this.todo = todo;
        this.date = date;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}