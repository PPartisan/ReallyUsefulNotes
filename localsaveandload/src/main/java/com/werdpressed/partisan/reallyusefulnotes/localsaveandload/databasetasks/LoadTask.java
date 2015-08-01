package com.werdpressed.partisan.reallyusefulnotes.localsaveandload.databasetasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.werdpressed.partisan.reallyusefulnotes.localsaveandload.R;

import java.lang.ref.WeakReference;

public class LoadTask extends AsyncTask<Void, Void, Cursor> {

    private static final String TAG = "LoadTask";

    private WeakReference<Context> weakReference;
    private LoadComplete loadComplete;
    private FilesDatabaseHelper db;
    private AlertDialog dialog;

    public interface LoadComplete {
        void cursorReady(Cursor cursor);
    }

    public LoadTask(Context context) {
        weakReference = new WeakReference<>(context);
        db = FilesDatabaseHelper.getInstance(weakReference.get());
        try {
            loadComplete = (LoadComplete) weakReference.get();
        } catch (ClassCastException e) {
            Log.e(TAG, context.toString() + " must implement LoadComplete");
        }
        buildDialog();
    }

    @Override
    protected Cursor doInBackground(Void... params) {
        Cursor result = db.getReadableDatabase().query(
                FilesDatabaseHelper.TABLE,
                null, null, null, null, null, FilesDatabaseHelper.TITLE);
        result.getCount();
        return result;
    }

    @Override
    protected void onPreExecute() {
        dialog.show();
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        loadComplete.cursorReady(cursor);
        dialog.dismiss();
    }

    private void buildDialog() {
        dialog = new AlertDialog.Builder(weakReference.get(), R.style.AlertDialogStyle)
                .setTitle(weakReference.get().getString(R.string.app_name))
                .setMessage(weakReference.get().getString(R.string.loading))
                .setPositiveButton(weakReference.get().getString(R.string.cancel), null)
                .create();
    }
}
