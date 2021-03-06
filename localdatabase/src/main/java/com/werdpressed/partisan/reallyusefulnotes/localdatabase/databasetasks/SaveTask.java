package com.werdpressed.partisan.reallyusefulnotes.localdatabase.databasetasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class SaveTask extends AsyncTask<String, Void, Void> {

    private WeakReference<Context> weakReference;
    private FilesDatabaseHelper db;
    private LoadTask lt;
    private long keyId;

    public SaveTask(Context context, long keyId){
        weakReference = new WeakReference<>(context);
        db = FilesDatabaseHelper.getInstance(weakReference.get());
        this.keyId = keyId;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        lt = new LoadTask(weakReference.get());
        lt.execute();
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
