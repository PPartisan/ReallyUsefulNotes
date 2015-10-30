package com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.NoteRowItem;

import java.lang.ref.WeakReference;


public class AddTask extends AsyncTask<NoteRowItem, Void, NoteRowItem> {

    private WeakReference<TaskCallbacks> mWeakCallback;
    private FilesDatabaseHelper db;

    public AddTask(Context context){
        mWeakCallback = new WeakReference<>((TaskCallbacks)context);
        db = FilesDatabaseHelper.getInstance(context);
    }

    @Override
    protected NoteRowItem doInBackground(NoteRowItem... params) {
        ContentValues cv = new ContentValues();
        cv.put(FilesDatabaseHelper.TITLE, params[0].getTitle());
        cv.put(FilesDatabaseHelper.CONTENT, params[0].getContent());
        cv.put(FilesDatabaseHelper.LIST_ORDER, params[0].getListOrder());

        long keyId = db.getWritableDatabase().insert(FilesDatabaseHelper.TABLE, null, cv);
        params[0].setKeyId(keyId);

        return params[0];
    }

    @Override
    protected void onPostExecute(NoteRowItem item) {
        mWeakCallback.get().addNewNoteToNoteRowItems(item);
    }
}
