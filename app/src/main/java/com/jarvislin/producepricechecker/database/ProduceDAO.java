package com.jarvislin.producepricechecker.database;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jarvislin.producepricechecker.ProduceData;


/**
 * Created by JarvisLin on 14/11/22.
 */
public class ProduceDAO {
    // 表格名稱
    public static final String TABLE_NAME = "item";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "id";

    // 其它表格欄位名稱
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String TOP_PRICE = "top_price";
    public static final String MID_PRICE = "mid_price";
    public static final String LOW_PRICE = "low_price";
    public static final String AVG_PRICE = "avg_price";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " TEXT, " +
                    TYPE + " TEXT, " +
                    TOP_PRICE + " TEXT NOT NULL, " +
                    MID_PRICE + " TEXT NOT NULL, " +
                    LOW_PRICE + " TEXT NOT NULL, " +
                    AVG_PRICE + " TEXT NOT NULL " +
                    ")";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public ProduceDAO(Context context) {
        db = DBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public ProduceData insert(ProduceData produceData) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(NAME, produceData.getName());
        cv.put(TYPE, produceData.getType());
        cv.put(TOP_PRICE, produceData.getTopPrice());
        cv.put(MID_PRICE, produceData.getMidPrice());
        cv.put(LOW_PRICE, produceData.getLowPrice());
        cv.put(AVG_PRICE, produceData.getAvgPrice());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
//        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
//        item.setId(id);
        // 回傳結果
        return produceData;
    }

    // 修改參數指定的物件
//    public boolean update(Item item) {
//        // 建立準備修改資料的ContentValues物件
//        ContentValues cv = new ContentValues();
//
//        // 加入ContentValues物件包裝的修改資料
//        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
//        cv.put(DATETIME_COLUMN, item.getDatetime());
//        cv.put(COLOR_COLUMN, item.getColor().parseColor());
//        cv.put(TITLE_COLUMN, item.getTitle());
//        cv.put(CONTENT_COLUMN, item.getContent());
//        cv.put(FILENAME_COLUMN, item.getFileName());
//        cv.put(LATITUDE_COLUMN, item.getLatitude());
//        cv.put(LONGITUDE_COLUMN, item.getLongitude());
//        cv.put(LASTMODIFY_COLUMN, item.getLastModify());
//
//        // 設定修改資料的條件為編號
//        // 格式為「欄位名稱＝資料」
//        String where = KEY_ID + "=" + item.getId();
//
//        // 執行修改資料並回傳修改的資料數量是否成功
//        return db.update(TABLE_NAME, cv, where, null) > 0;
//    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 讀取所有記事資料
    public List<ProduceData> getAll() {
        List<ProduceData> result = new ArrayList<ProduceData>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public ProduceData get(long id) {
        // 準備回傳結果用的物件
        ProduceData item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    // 把Cursor目前的資料包裝為物件
    public ProduceData getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        String[] data = new String[6];

        data[0] = cursor.getString(1);
        data[1] = cursor.getString(2);
        data[2] = cursor.getString(3);
        data[3] = cursor.getString(4);
        data[4] = cursor.getString(5);
        data[5] = cursor.getString(6);



        // 回傳結果
        return new ProduceData(data);
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }


}
