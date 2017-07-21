package com.food.lmln.food.db;


import android.util.Log;

import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import static com.food.lmln.food.db.Constant.CONSUMPTIONID;
import static com.food.lmln.food.db.Constant.DESKTEMP_TIME;
import static com.food.lmln.food.db.Constant.DESK_TEMP;

/**
 * Created by Weili on 2017/7/4.
 * MysqlDbManger
 */

public class MySqlDbManger {

    // 静态成员，支持单态模式
    private static MySqlDbManger per = null;
    private Connection conn = null;
    private Statement stmt = null;

    // 单态模式-懒汉模式
    public MySqlDbManger() {
    }
    public static MySqlDbManger createInstance() {
        if (per == null) {
            per = new MySqlDbManger();
            per.initDB();
        }
        return per;
    }

    // 加载驱动
    public void initDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 连接数据库，获取句柄+对象
    public java.sql.Connection connectDB() {
        System.out.println("Connecting to database...");
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://103.45.11.232/lm_food",
                    "root","root");
            stmt = (Statement) conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("SqlManager:Connect to database successful.");
        return null;
    }


    public static void query(java.sql.Connection conn, String sql) {
        if (conn == null) {
            return;
        }
        java.sql.Statement statement = null;
        java.sql.ResultSet result = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            if (result != null && result.first()) {
                int idColumnIndex = result.findColumn("id");
//                int nameColumnIndex = result.findColumn("meat_name");
                while (!result.isAfterLast()) {
                    Log.d("Util", "_____"+sql);
//                    System.out.println(result.getString(nameColumnIndex));
                    result.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }} catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }

    public static boolean execSQL(java.sql.Connection conn, String sql) {
        boolean execResult = false;
        if (conn == null) {
            return execResult;
        }
        java.sql.Statement statement = null;
        try {
            statement = conn.createStatement();
            if (statement != null) {
                execResult = statement.execute(sql);
            }
        } catch (SQLException e) {
            execResult = false;
        }
        return execResult;
    }
}



