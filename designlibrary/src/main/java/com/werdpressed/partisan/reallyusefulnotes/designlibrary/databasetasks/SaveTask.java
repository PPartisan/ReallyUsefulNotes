package com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.NoteFragmentCallbacks;

import java.lang.ref.WeakReference;

public class SaveTask extends AsyncTask<String, Void, Void> {

    private WeakReference<Context> weakReference;
    private FilesDatabaseHelper db;
    private long keyId;

    public SaveTask(Context context, long keyId){
        weakReference = new WeakReference<>(context);
        db = FilesDatabaseHelper.getInstance(weakReference.get());
        this.keyId = keyId;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //ToDo Run LoadTask to refresh data...but no need to update UI?
        //((NoteFragmentCallbacks)weakReference.get()).launchLoadCursorTask();
    }

    @Override
    protected Void doInBackground(String... params) {

        ContentValues cv = new ContentValues();
        cv.put(FilesDatabaseHelper.CONTENT, params[0]);

        db.getWritableDatabase().update(
                FilesDatabaseHelper.TABLE,
                cv,
                FilesDatabaseHelper.KEY_ID + "=?",
                new String[]{String.valueOf(keyId)});

        return null;
    }

}
