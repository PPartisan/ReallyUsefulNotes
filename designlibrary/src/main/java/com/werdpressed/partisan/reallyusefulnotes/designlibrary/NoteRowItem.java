package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import java.util.Comparator;

public class NoteRowItem {

    private String title, content;
    private int keyId, listOrder;

    public NoteRowItem(){
        this("New Note", "", 0, 0);
    }

    public NoteRowItem(String title, String content, int listOrder, int keyId) {
        this.title = title;
        this.content = content;
        this.listOrder = listOrder;
        this.keyId = keyId;
    }

    public static final Comparator<NoteRowItem> titleComparator = new Comparator<NoteRowItem>() {
        @Override
        public int compare(NoteRowItem lhs, NoteRowItem rhs) {
            return (lhs.title).compareToIgnoreCase(rhs.title);
        }
    };

    public static final Comparator<NoteRowItem> listOrderComparator = new Comparator<NoteRowItem>() {
        @Override
        public int compare(NoteRowItem lhs, NoteRowItem rhs) {
            return lhs.listOrder - rhs.listOrder;
        }
    };

    public static final Comparator<NoteRowItem> keyIdComparator = new Comparator<NoteRowItem>() {
        @Override
        public int compare(NoteRowItem lhs, NoteRowItem rhs) {
            return lhs.keyId - rhs.keyId;
        }
    };

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

    public int getListOrder() {
        return listOrder;
    }

    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

}
