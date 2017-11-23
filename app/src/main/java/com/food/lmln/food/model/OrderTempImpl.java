package com.food.lmln.food.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.food.lmln.food.bean.OrderTemp;
import com.food.lmln.food.db.SqlHelper;



/**
 * 临时订单的操作。
 *  @author Weli
 *  @time 2017-11-23  9:49
 *  @describe
 */
public class OrderTempImpl implements OrderTempDao {

    private SqlHelper dbHelper;
    public OrderTempImpl(SqlHelper dbHelper) {
        super();
        this.dbHelper = dbHelper;
    }

    @Override
    public void setOrderTemp(OrderTemp temp) {
        SQLiteDatabase sdb = dbHelper.getWritableDatabase();
        String sql = "Insert into desk_temp Values(1,'"+temp.getOrder_temp()+"');";
        sdb.execSQL(sql);
        sdb.close();
    }

    @Override
    public String getOrderTemp() {

        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "Select  * from  desk_temp  where id=1 ";
        Cursor cursor = sdb.rawQuery(sql, null);
        Log.d("OrderTempImpl", sql);
        String orderTemp =null;
        if(cursor!=null){
            while(cursor.moveToNext()){
                orderTemp = cursor.getString(cursor.getColumnIndex("order_temp"));
                Log.d("OrderTempImpl", orderTemp);
            }
            cursor.close();
        }
        sdb.close();
        return orderTemp;
    }

    @Override
    public String delOrderTemp(OrderTemp temp) {
        return null;
    }
    @Override
    public void updOrderTemp(OrderTemp temp) {
        SQLiteDatabase sdb = dbHelper.getWritableDatabase();
        String sql = "update  desk_temp set order_temp='" +temp.getOrder_temp()+ "' where id=1";
        sdb.execSQL(sql);
        sdb.close();
    }
}
