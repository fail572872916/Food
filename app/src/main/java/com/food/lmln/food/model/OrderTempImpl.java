package com.food.lmln.food.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.food.lmln.food.bean.OrderTemp;
import com.food.lmln.food.db.SqlHelper;


/**
 * Created by Administrator on 17/9/3.
 * 临时订单的操作。
 */

public class OrderTempImpl implements OrderTempDao {

    private SqlHelper dbHelper;
    public OrderTempImpl(SqlHelper dbHelper) {
        super();
        this.dbHelper = dbHelper;
    }

    @Override
    public void setOrderemp(OrderTemp temp) {
        SQLiteDatabase sdb = dbHelper.getWritableDatabase();
        String sql = "Insert into desk_temp Values(1,'"+temp.getOrder_temp()+"');";
        sdb.execSQL(sql);
        sdb.close();
    }

    @Override
    public String getOrderemp() {

        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "Select  * from  desk_temp  where id=1 ";
        Cursor cursor = sdb.rawQuery(sql, null);
        Log.d("OrderTempImpl", sql);
        String  orderemp=null;
        if(cursor!=null){
            while(cursor.moveToNext()){
                 orderemp = cursor.getString(cursor.getColumnIndex("order_temp"));
                Log.d("OrderTempImpl", orderemp);
            }
            cursor.close();
        }
        sdb.close();
        return orderemp;
    }

    @Override
    public String delOrderemp(OrderTemp temp) {
        return null;
    }
    @Override
    public void updOrderemp(OrderTemp temp) {
        SQLiteDatabase sdb = dbHelper.getWritableDatabase();
        String sql = "update  desk_temp set order_temp='" +temp.getOrder_temp()+ "' where id=1";
        sdb.execSQL(sql);
        sdb.close();
    }
}
