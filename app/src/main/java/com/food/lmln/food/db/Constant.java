package com.food.lmln.food.db;

/**
 * Created by Weili on 2017/6/14.
 */

public class Constant {


    /**
     * MyslSqlConnection
     */
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static  final   String  SQLURL="jdbc:mysql://103.45.11.232/lm_food";
    public static  final   String  USERNAME="root";
    public static  final   String  PASSWORD="root";

    public static  final String  DESK_TEMP ="lm_food.desk_temp" ;//临时点菜
    public static  final String  CONSUMPTIONID ="consumptionID" ;//临时点菜

    public static  final String  ORDERID ="order_id" ;//订单号
    public static  final String  DSK_NO ="desk_no" ;//订单号
    public static  final String  DESKTEMP_TIME ="time" ;//订单号
    public static  final String  ORDERINFO ="order_info" ;//订单表


    public static  final  String  FILENAME="conTumpTionidOrder.dat";


    public static  final   String  DATABASE_NAME="lm_foodsql.db";//数据库的名称
    public static  final   int  DATABASE_VERSION=1 ;//数据库的版本
    public static  final   String  TABLE_NAME="" ;//数据库的名称
//    public static final String DB_NAME = "china_cities.db";
   public   static final String ASSETS_NAME = DATABASE_NAME;
    public static final String TABLE_NAME_DESCRIBE = "describe";
    public static final String TABLE_NAME_FOODINFO = "fd_meat";


    public static final String MID = "mid";
    public static final String MEAT_NAME = "meat_name";
    public static final String MEAT_DESCRIBE = "meat_describe";
    public static final String MEAT_PRICE = "meat_price";
    public static final String MEAT_MID = "meat_mid";
    public static final String MEAT_IMAGE = "meat_image";


    public static final int BUFFER_SIZE = 1024;


    public static final int send_msg_code1 = 0x101;
    public static final int send_msg_code2 = 0x102;
    public static final int send_msg_code3 = 0x103;
    public static final int send_msg_code4 = 0x104;
    public static final int send_msg_code5 = 0x105;
    public static final int send_msg_code6 = 0x106;
    public static final int send_msg_code7 = 0x107;
    public static final int send_msg_code8 = 0x108;


}
