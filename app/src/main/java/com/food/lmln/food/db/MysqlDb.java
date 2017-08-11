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
import static com.food.lmln.food.utils.FileUtils.rewriteOrdera;

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

    /**
     * 查询最后一个订单
     * @param conn
     * @param tableName    Constant.ORDERID
     * @return
     */
    public  static int  selectEndOrder(Connection conn, String  tableName ,String orderInfo ,String orderId){
//        String  sql   ="select * from  "+tableName+" limit 0,1";
        String sql = "select "+tableName+" from "+orderInfo+" order by "+orderId+" desc limit 0,1;";
         int  num =0;
        if (conn == null) {
            return Integer.parseInt(null);
        }
        Statement statement = null;
        ResultSet result = null;
//        list=null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            while(result.next()){//将结果集信息添加到返回向量中
                num=1;
                String food_name =result.getString(Constant.ORDER_INFO);
                rewriteOrdera(food_name);
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

        return   num;
    }


    /**
     * 查询是否开台
     * @param conn
     * @param sql
     * @return
     */
    public static  String   selectDeskNO(Connection conn, String sql) {
        String  status =null;
        if (conn == null) {
            return (String) null;
        }
        Statement statement = null;
        ResultSet result = null;
//        list=null;
        int a=0;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            if (result.next()) {
                //存在记录 rs就要向上移一条记录 因为rs.next会滚动一条记录了
                result.previous();
                //在执行while 循环
                while(result.next()){
                    status=result.getString(Constant.SHOP_STATUS);
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
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return   status;
    }


    /**
     * 查当前桌台的订单号
     * @param conn
     * @param sql
     * @return
     */
    public static  String  selectByNo(Connection conn, String sql) {
        String  status =null;
        if (conn == null) {
            return (String) null;
        }
        Statement statement = null;
        ResultSet result = null;
//        list=null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            if (result.next()) {
                //存在记录 rs就要向上移一条记录 因为rs.next会滚动一条记录了
                result.previous();
                //在执行while 循环
                while(result.next()){
                    status=result.getString(Constant.CONSUMPTIONID);
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
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return   status;
    }





    /**
     * 查询最近的订单
     * @param conn
     * @param sql
     * @return
     */
    public static  String   selectOrderTemp(Connection conn, String sql) {
        String  status =null;
        if (conn == null) {
            return (String) null;
        }
        Statement statement = null;
        ResultSet result = null;
//        list=null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            if (result.next()) {
                //存在记录 rs就要向上移一条记录 因为rs.next会滚动一条记录了
                result.previous();
                //在执行while 循环
                while(result.next()){
                    status=result.getString(Constant.ORDER_ID);
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
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return   status;
    }
    /**
     * 进行增删改查操作，用于判断操作成功
     * @param conn
     * @param sql
     * @return
     */
    public static  int   exuqueteUpdate(Connection conn, String sql) {
        if (conn == null) {
            return  (Integer) null;
        }
        Statement statement = null;
        int result = 0;
//        list=null;

        try {
            statement = conn.createStatement();
            result = statement.executeUpdate(sql);
            int a=result;
            Log.d("MysqlDb", "a:" + a);
            if(result!=-1){
                result=1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return   result;
    }
    /**
     * 分页查询
     * @param conn
     * @param tableName
     * @param pageIndex
     * @param pageSize
     * @return
     */
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

    /**
     *  右侧菜单的数据
     * @param conn
     * @param sql
     * @return
     */
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