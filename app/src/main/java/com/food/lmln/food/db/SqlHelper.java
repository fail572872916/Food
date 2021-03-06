package com.food.lmln.food.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Weili on 2017/6/14.
 * Sqlite
 */

public class SqlHelper extends SQLiteOpenHelper {


    /**
     *  构造函数
     * @param context  上下文对象
     * @param name   表示创建数据库的名称
     * @param factory  游标工厂
     * @param version  数据库当前版本 >=1
     */
    public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public SqlHelper(Context context) {
        super(context, Constants.DATABASE_NAME,null, Constants.DATABASE_VERSION);
    }
    /**
     * 数据库创建时调用
     * @param db  数据库对象
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table desk_info("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "local_ip  TEXT(60),"
                + "local_desk  TEXT(60) );");
        db.execSQL("create table  desk_temp(id integer primary key autoincrement not null,order_temp text )");
        db.execSQL("Insert into desk_temp Values(1,'');");

    }
    /**
     *   版本更行时
     * @param db  数据库对象
     * @param oldVersion  数据库旧版
     * @param newVersion  新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("SqlHelper", "onUpgrade");
    }
    /**
     * 数据库打开时调用
     * @param db  数据库对象
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("SqlHelper", "open");
    }
}
