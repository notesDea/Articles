package com.notesdea.articles.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.notesdea.articles.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by notes on 2016/12/11.
 */
//存储缓存的数据
public class CachedDBHelper extends SQLiteOpenHelper {

    //表单名
    public static final String TABLE_NAME = "articles";
    //id
    public static final String ID = "_id";
    public static final String ID_ATTR = "integer primary key autoincrement";
    //url
    public static final String URL = "url";
    public static final String URL_ATTR = "text";
    //data
    public static final String DATA = "data";
    public static final String DATA_ATTR = "text";
    //time
    public static final String TIME = "time";
    public static final String TIME_ATTR = "text";
    //创建表格信息
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " (" +
            ID + " " + ID_ATTR + ", " +
            URL + " " + URL_ATTR + ", " +
            DATA + " " + DATA_ATTR + ", " +
            TIME + " " + TIME_ATTR + ")";

    //数据库名
    public static final String DB_NAME = "Articles.db";
    //数据库的版本
    public static final int DB_VERSION = 1;



    public CachedDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
