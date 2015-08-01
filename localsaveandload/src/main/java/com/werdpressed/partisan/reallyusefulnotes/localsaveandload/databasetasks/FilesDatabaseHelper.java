package com.werdpressed.partisan.reallyusefulnotes.localsaveandload.databasetasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FilesDatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "files.db";

    private static final int SCHEMA = 1;

    public static final String TABLE = "files";
    public static final String KEY_ID = "_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    private static FilesDatabaseHelper mInstance = null;

    public static synchronized FilesDatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FilesDatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public FilesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE files " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    break;
            }
            upgradeTo++;
        }
    }
}
