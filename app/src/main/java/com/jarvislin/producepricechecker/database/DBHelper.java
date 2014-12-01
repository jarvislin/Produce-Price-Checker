package com.jarvislin.producepricechecker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jarvis Lin on 2014/11/22.
 */
public class DBHelper extends SQLiteOpenHelper {

    // 資料庫名稱
    public static final String DATABASE_NAME = "data.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new DBHelper(context, DATABASE_NAME,
                    null, VERSION).getWritableDatabase();
        }

        return database;
    }

    public static void dropTable(Context context){
        SQLiteDatabase db = getDatabase(context);
        db.execSQL("DROP TABLE IF EXISTS " + ProduceDAO.TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ProduceDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProduceDAO.TABLE_NAME);
        onCreate(db);
    }
}
