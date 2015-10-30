package com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.NoteFragmentCallbacks;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.NoteRowItem;

import java.lang.ref.WeakReference;

public class SaveTask extends AsyncTask<NoteRowItem, Void, Void> {

    private FilesDatabaseHelper db;

    public SaveTask(FilesDatabaseHelper db){
        this.db = db;
    }

    @Override
    protected Void doInBackground(NoteRowItem... params) {

        ContentValues cv = new ContentValues();
        cv.put(FilesDatabaseHelper.TITLE, params[0].getTitle());
        cv.put(FilesDatabaseHelper.CONTENT, params[0].getContent());

        db.getWritableDatabase().update(
                FilesDatabaseHelper.TABLE,
                cv,
                FilesDatabaseHelper.KEY_ID + "=?",
                new String[]{String.valueOf(params[0].getKeyId())});

        return null;
    }

}
