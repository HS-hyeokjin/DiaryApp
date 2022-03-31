package com.example.diaryapp;

public class Diary {
    int _id;
    String diaryContent;

    public Diary(int _id, String diaryContent) {
        this._id = _id;
        this.diaryContent = diaryContent;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDiaryContent() {
        return diaryContent;
    }

    public void setDiaryContent(String diaryContent) {
        this.diaryContent = diaryContent;
    }

}