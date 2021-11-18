package com.cm.milestone2;

public class NoteItemClass {

    public  String id;
    public  String content;
    public  String details;

    public NoteItemClass(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}

