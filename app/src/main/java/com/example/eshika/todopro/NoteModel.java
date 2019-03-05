package com.example.eshika.todopro;

public class NoteModel {
    public String Notetitle;
    public String Notetime;

    public NoteModel(String notetitle, String notetime) {
        Notetitle = notetitle;
        Notetime = notetime;
    }

    public String getNotetitle() {
        return Notetitle;
    }

    public void setNotetitle(String notetitle) {
        Notetitle = notetitle;
    }

    public String getNotetime() {
        return Notetime;
    }

    public void setNotetime(String notetime) {
        Notetime = notetime;
    }
}
