package com.food.lmln.food.db;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.food.lmln.food.bean.DeskInfo;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.bean.MenuButton;
import com.food.lmln.food.bean.OrderInfo;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.food.lmln.food.db.Constant.DESK_TEMP;
import static com.food.lmln.food.db.Constant.DSK_NO;
import static com.food.lmln.food.db.Constant.send_msg_code4;

/**
 * Created by Weili on 2017/7/14.
 */

public class MysqlDb {
    final static String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static Connection openConnection(String url, String user,
                                            String password) {
        Connection conn = null;
        try {
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            conn = null;
        } catch (SQLException e) {
            conn = null;
        }

        return conn;
    }

    /**
     * 查询
     * @param conn 连接对象
     * @param sql 查询语句
     * @return
     */
    public static  String   query(Connection conn, String sql) {
        String isNull = null;
        if (conn == null) {
            return null;
        }
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            while(result.next()){//将结果集信息添加到返回向量中
                isNull=result.getString("order_id");
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
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return   isNull;
    }

    public static boolean execSQL(Connection conn, String sql) {
        boolean execResult = false;
        if (conn == null) {
            return execResult;
        }
        Statement statement = null;
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


    /**
     * 查询左侧菜单
     * @param conn 连接对象
     * @param sql  sql语句
     * @return  返回一个List
     */
    public static  List<MenuButton>   selectCuisine(Connection conn, String sql) {
        List<MenuButton>  list=  new ArrayList<MenuButton>();

        if (conn == null) {
            return null;
        }
        Statement statement = null;
        ResultSet result = null;
//        list=null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            while(result.next()){//将结果集信息添加到返回向量中
                String cuisine_name =result.getString("cuisine_name");
                String cuisine_describe =result.getString("cuisine_describe");
                String   describe_id = result.getString("describe_id");
                String   describe_version = result.getString("describe_version");

                list.add(   new MenuButton(0,cuisine_name,cuisine_describe,describe_id,describe_version));
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
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return   list;
    }

    /**
     * 查询表
     * @param conn
     * @param sql
     * @return
     */
    public static  List<FoodinfoSmall>   selectFood(Connection conn, String sql) {
        Log.d("MysqlDb", sql);
        List<FoodinfoSmall>  list=  new ArrayList<FoodinfoSmall>();
        if (conn == null) {
            return null;
        }
        Statement statement = null;
        ResultSet result = null;
//        list=null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            while(result.next()){//将结果集信息添加到返回向量中
                String food_name =result.getString("food_name");
                String food_describe =result.getString("food_describe");
                String   food_Price = result.getString("food_Price");
                String   food_image = result.getString("food_image");
                list.add(   new FoodinfoSmall(0,food_name,food_describe,food_Price,food_image));
                Log.d("MysqlDb", "list:" + list);
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
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return   list;
    }
    public static  List<FoodinfoSmall>   ByPageIndex(Connection conn, String tableName ,int  pageIndex ,int pageSize ) {

        int index = (pageIndex-1) * pageSize;
        String  sql   ="select * from  "+tableName+" limit "+index+","+pageSize+"";
        Log.d("MysqlDb", sql);
        List<FoodinfoSmall>  list=  new ArrayList<FoodinfoSmall>();
        if (conn == null) {
            return null;
        }
        Statement statement = null;
        ResultSet result = null;
//        list=null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            while(result.next()){//将结果集信息添加到返回向量中
                String food_name =result.getString("food_name");
                String food_describe =result.getString("food_describe");
                String   food_Price = result.getString("food_Price");
                String   food_image = result.getString("food_image");

                list.add(   new FoodinfoSmall(0,food_name,food_describe,food_Price,food_image));
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
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }

        return   list;
    }


    public static  List<OrderInfo>   selectRiht(Connection conn, String sql) {
        List<OrderInfo> newList=new ArrayList<OrderInfo>(); ;

        int count=1;
        if (conn == null) {
            return null;
        }
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            while (result.next()){
                //                String data=rs.getString("date");
//                String time=rs.getString("time");
//                String desk_no=rs.getString("desk_no");
//                String consumptionID=rs.getString("consumptionID");
                String foodName=result.getString("foodName");
                String foodPrice=result.getString("foodPrice");
                int foodCount=result.getInt("foodCount");
                OrderInfo info=new OrderInfo(count++,foodName,Double.valueOf(foodPrice),foodCount,false);
                newList.add(info);

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
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return   newList;
    }



}