package com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.NoteFragmentCallbacks;
import com.werdpressed.partisan.reallyusefulnotes.designlibrary.NoteRowItem;

import java.lang.ref.WeakReference;

public class SaveTask extends AsyncTask<NoteRowItem, Void, Void> {

    private FilesDatabaseHelper db;
    private long keyId;

    public SaveTask(Context context, long keyId){
        db = FilesDatabaseHelper.getInstance(context);
        this.keyId = keyId;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //ToDo Run LoadTask to refresh data...but no need to update UI?
        //((NoteFragmentCallbacks)weakReference.get()).launchLoadCursorTask();
    }

    @Override
    protected Void doInBackground(NoteRowItem... params) {

        ContentValues cv = new ContentValues();
        cv.put(FilesDatabaseHelper.TITLE, params[0].getTitle());
        cv.put(FilesDatabaseHelper.CONTENT, params[0].getContent());
        cv.put(FilesDatabaseHelper.LIST_ORDER, params[0].getKeyId());

        db.getWritableDatabase().update(
                FilesDatabaseHelper.TABLE,
                cv,
                FilesDatabaseHelper.KEY_ID + "=?",
                new String[]{String.valueOf(keyId)});

        return null;
    }

}
