package com.werdpressed.partisan.reallyusefulnotes.designlibrary.databasetasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FilesDatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "files.db";

    private static final int SCHEMA = 2;

    public static final String TABLE = "files";
    public static final String KEY_ID = "_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String LIST_ORDER = "list_order"; //Added in v2
    public static final String PRIORITY = "priority"; //Added in v2

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
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT, " +
                "list_order INTEGER, priority INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    db.execSQL("ALTER TABLE files ADD COLUMN list_order INTEGER DEFAULT 0");
                    db.execSQL("ALTER TABLE files ADD COLUMN priority INTEGER DEFAULT 0");
                    break;
            }
            upgradeTo++;
        }
    }
}
