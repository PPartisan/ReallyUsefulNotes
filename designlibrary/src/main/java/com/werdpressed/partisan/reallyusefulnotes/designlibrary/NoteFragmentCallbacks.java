package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import java.util.ArrayList;

public interface NoteFragmentCallbacks {

    ArrayList<NoteRowItem> requestNotes();

    void deleteNote(int position);
    void moveNote(int fromPosition, int toPosition);
    void updateNoteListOrders();

}
