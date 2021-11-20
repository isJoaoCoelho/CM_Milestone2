package com.cm.milestone2;

import android.provider.ContactsContract;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    String title;
    String content;
    List<NoteItemClass> list = new ArrayList<>();
    String id;
    NoteItemClass item;
    Boolean haveUpdate = false;

    public Boolean getHaveUpdate() {
        return haveUpdate;
    }

    public void setHaveUpdate(Boolean haveUpdate) {
        this.haveUpdate = haveUpdate;
    }

    public NoteItemClass getItem() {
        return item;
    }

    public void setItem(NoteItemClass item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<NoteItemClass> getList() {
        return list;
    }

    public void setList(List<NoteItemClass> list) {
        this.list = new ArrayList<>(list);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}