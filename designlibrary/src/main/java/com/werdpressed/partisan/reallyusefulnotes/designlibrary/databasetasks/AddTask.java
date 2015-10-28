package com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.NoteRowItem;


public class AddTask extends AsyncTask<NoteRowItem, Void, Long> {

    private FilesDatabaseHelper db;

    public AddTask(Context context){
        db = FilesDatabaseHelper.getInstance(context);
    }

    @Override
    protected Long doInBackground(NoteRowItem... params) {
        ContentValues cv = new ContentValues();
        cv.put(FilesDatabaseHelper.TITLE, params[0].getTitle());
        cv.put(FilesDatabaseHelper.CONTENT, params[0].getContent());
        cv.put(FilesDatabaseHelper.LIST_ORDER, params[0].getListOrder());
        return db.getWritableDatabase().insert(FilesDatabaseHelper.TABLE, null, cv);
    }
}
