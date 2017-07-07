package com.food.lmln.food.db;

/**
 * Created by Weili on 2017/7/4.
 */

import java.sql.ResultSet;
import java.sql.SQLException;



public class Service {

    public Boolean login( String table, int desk_no) {

        // 获取Sql查询语句
        String logSql = "select * from "+table+" where desk_no ='" + desk_no
                ;

        // 获取DB对象
        MySqlDbManger sql = MySqlDbManger.createInstance();
        sql.connectDB();

        // 操作DB对象
        try {
            ResultSet rs = sql.executeQuery(logSql);
            if (rs.next()) {
                sql.closeDB();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql.closeDB();
        return false;
    }

    public Boolean upOrders(String data, String time ,int desk_no , String consumptionID ,String foodName ,String foodPrice ,int  foodCount ) {

        // 获取Sql查询语句
        String regSql = "insert into student values('"+ data+ "','"+ time+ "',desk_no,'"+consumptionID+"','"+foodName+"','"+foodPrice+"',foodCount) ";

        // 获取DB对象
        MySqlDbManger sql = MySqlDbManger.createInstance();
        sql.connectDB();

        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDB();
            return true;
        }
        sql.closeDB();

        return false;
    }
}