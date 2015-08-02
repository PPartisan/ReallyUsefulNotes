package com.werdpressed.partisan.reallyusefulnotes.localdatabase.databasetasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;


public class AddTask extends AsyncTask<String, Void, Long> {

    private WeakReference<Context> weakReference;
    private FilesDatabaseHelper db;
    private LoadTask lt;

    private AddComplete addComplete;

    public interface AddComplete {
        void addComplete(Long newKeyId);
    }

    public AddTask(Context context){
        weakReference = new WeakReference<>(context);
        db = FilesDatabaseHelper.getInstance(weakReference.get());
        addComplete = (AddComplete) context;
    }

    @Override
    protected void onPostExecute(Long newKeyId) {
        addComplete.addComplete(newKeyId);
        lt = new LoadTask(weakReference.get());
        lt.execute();
    }

    @Override
    protected Long doInBackground(String... params) {
        ContentValues cv = new ContentValues();
        cv.put(FilesDatabaseHelper.TITLE, params[0]);
        return db.getWritableDatabase().insert(FilesDatabaseHelper.TABLE, null, cv);
    }

}
