package com.werdpressed.partisan.reallyusefulnotes.localsaveandload.databasetasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;


public class AddTask extends AsyncTask<String, Void, Void> {

    private WeakReference<Context> weakReference;
    private FilesDatabaseHelper db;

    public AddTask(Context context){
        weakReference = new WeakReference<>(context);
        db = FilesDatabaseHelper.getInstance(weakReference.get());
    }



    @Override
    protected Void doInBackground(String... params) {
        ContentValues cv = new ContentValues();
        cv.put(FilesDatabaseHelper.TITLE, params[0]);
        long id = db.getWritableDatabase().insert(FilesDatabaseHelper.TABLE, null, cv);
        Log.e("AddTask", "value is " + id);
        return null;
    }

}
