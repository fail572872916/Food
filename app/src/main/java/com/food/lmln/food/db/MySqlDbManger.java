package com.food.lmln.food.db;


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



    // 关闭数据库 关闭对象，释放句柄
    public void closeDB() {
        System.out.println("Close connection to database..");
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Close connection successful");
    }

    // 查询
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            rs = (ResultSet) stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }



    //知道学生的学号得到他的密码
//    public static String selectPwd()
//    {
//        String result="";
//        try
//        {
//            Connection con=getConnection();
//            java.sql.Statement st=con.createStatement();
//            String sql = "select "+CONSUMPTIONID+" from "+DESK_TEMP+" order by "+DESKTEMP_TIME+" desc limit 0,1;";
//            java.sql.ResultSet rs=st.executeQuery(sql);
//            if(rs.next())
//            {
//                result=rs.getString("CONSUMPTIONID");
//
//            }
//            rs.close();
//            st.close();
//            con.close();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        return result;
//    }
//    public static int selectClear()  {
//        int num = 0;
//        ResultSet rs = null;
//        try {
//            String sql = "select "+CONSUMPTIONID+" from "+DESK_TEMP+" order by "+DESKTEMP_TIME+" desc limit 0,1;";
//            rs=stmt.executeQuery(sql);
//            if(rs.next()){
//                num=1;
//            }else{
//                num=2;
//            }
//            rs.close();
//            st.close();
//            con.close();
//        }   catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return num;
//    }

    // 增添/删除/修改
    public int executeUpdate(String sql) {
        int ret = 0;
        try {
            ret = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}



