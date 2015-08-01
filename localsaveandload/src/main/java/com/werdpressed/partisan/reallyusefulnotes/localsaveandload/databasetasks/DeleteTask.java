package com.werdpressed.partisan.reallyusefulnotes.localsaveandload.databasetasks;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class DeleteTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<Context> weakReference;
    private FilesDatabaseHelper db;
    private LoadTask lt;
    private int keyId;

    public DeleteTask(Context context, int keyId) {
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
    protected Void doInBackground(Void... params) {
        db.getWritableDatabase().delete(FilesDatabaseHelper.TABLE,
                FilesDatabaseHelper.KEY_ID + "=?",
                new String[] { String.valueOf(keyId) });
        return null;
    }
}
