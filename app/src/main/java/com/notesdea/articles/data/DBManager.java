package com.notesdea.articles.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.notesdea.articles.BaseApplication;
import com.notesdea.articles.model.PostsWithStatus;

/**
 * Created by notes on 2016/12/16.
 */

public class DBManager {
    private CachedDBHelper mDbHelper;
    private SQLiteDatabase mDb;

    public DBManager() {
        mDbHelper = new CachedDBHelper(BaseApplication.getContext());
    }

    /**
     * 插入数据
     * @param url 插入的url列
     * @param data 插入的json数据列
     */
    public synchronized void insertData(String url, String data) {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CachedDBHelper.URL, url);
        values.put(CachedDBHelper.DATA, data);
        values.put(CachedDBHelper.TIME, System.currentTimeMillis());
        mDb.insert(CachedDBHelper.TABLE_NAME, null, values);
        mDb.close();
    }

    /**
     * 获取缓存的数据
     * @param url 通过指定 url 来获取
     * @return 返回缓存的数据，是 json 类型的。
     */
    public synchronized String getData(String url) {
        String result = "";
        mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(
                "SELECT * FROM " + CachedDBHelper.TABLE_NAME + " WHERE URL = ?", new String[]{url});
        while (cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex(CachedDBHelper.DATA));
        }
        cursor.close();
        mDb.close();
        return result;
    }


}
