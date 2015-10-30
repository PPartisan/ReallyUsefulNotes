package com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks;

import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.NoteRowItem;

import java.util.ArrayList;

public class UpdateListOrderTask extends AsyncTask<ArrayList<NoteRowItem>, Void, Void> {

    private static final String UPDATE_STATEMENT = "UPDATE files SET list_order = ? WHERE _id = ?";

    FilesDatabaseHelper db;

    public UpdateListOrderTask(FilesDatabaseHelper db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(ArrayList<NoteRowItem>... params) {
        SQLiteStatement update = db.getWritableDatabase().compileStatement(UPDATE_STATEMENT);
        try {
            db.getWritableDatabase().beginTransaction();
            for (NoteRowItem item : params[0]) {
                update.bindLong(1, item.getListOrder());
                update.bindLong(2, item.getKeyId());
                update.execute();
            }
            db.getWritableDatabase().setTransactionSuccessful();
        } finally {
            db.getWritableDatabase().endTransaction();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        db = null;
    }
}
