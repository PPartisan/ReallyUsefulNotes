package com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks;

import android.database.Cursor;

public interface LoadCursorFragmentCallbacks {

    void sendDataToNoteRowItemsOperationsFragment(Cursor data);
}
