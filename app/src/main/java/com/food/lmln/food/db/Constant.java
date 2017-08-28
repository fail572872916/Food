package com.food.lmln.food.db;

/**
 * Created by Weili on 2017/6/14.
 */

public class Constant {


    public static final int SOCKET_PORT =30000;
    public static  final String SOCKETPARMAR = "0xFF";
    /**
     * MyslSqlConnection
     */
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static  final   String  SQLURL="jdbc:mysql://120.77.221.1:3036/lm_food";
    public static  final   String  USERNAME="root";
    public static  final   String  PASSWORD="lm123456";

    /**
     * 临时菜表
     */
    public static  final String  ORDERTABLE ="fd_describe" ;//临时点菜
    /**
     * Desk_temp  临时桌台
     */
    public static  final String  DESK_TEMP ="desk_temp" ;//临时点


    public static  final String  DESK_CONSUMPTIONID ="desk_consumptionid" ;//临时点菜的表
    public static  final String  CONSUMPTIONID ="consumptionID" ;//临时点菜的订单

    public static  final int   TEMP_PEOPLE = 2 ;//临时点菜的订单

    /**
     *临时订单表
     */

    public static  final String  ORDER_TEMP ="order_temp" ;//临时订单表
    public static  final String  ORDER_DATE ="order_date" ;//订单日期

    public static  final String  ORDER_ID ="order_id" ;//订单号
    public static  final String  DESKTEMP_TIME ="time" ;//订单号
    /**
     * OrderInfo
     */

    public static  final String  ORDER_INFO ="order_info" ;//订单表





    /**
     *Shopdeks
     */
    public static  final String  SHOP_DESK ="shop_desk" ;//开台
    public static  final String  DESK_NO ="desk_no" ;//桌台号
    public static  final String  SHOP_STATUS ="desk_status" ;//开台
    public static final String STATUS_WAIT ="待用" ;//待用
    public static final String STATUS_RUN ="使用" ;//待用

    /**
     * Sqlite
     */
    public static  final  String  FILENAME="conTumpTionidOrder.dat"; //订单变化文件
    public static  final   String  DATABASE_NAME="lm_foodsql.db";//数据库的名称
    public static  final   int  DATABASE_VERSION=1 ;//数据库的版本
    public static  final   String  TABLE_NAME="" ;//数据库的名称
    public   static final String ASSETS_NAME = DATABASE_NAME;
    public static final String TABLE_NAME_DESCRIBE = "describe";
    public static final String TABLE_NAME_FOODINFO = "fd_meat";


    public static final String MID = "mid";
    public static final String MEAT_NAME = "meat_name";
    public static final String MEAT_DESCRIBE = "meat_describe";
    public static final String MEAT_PRICE = "meat_price";
    public static final String MEAT_MID = "meat_mid";
    public static final String MEAT_IMAGE = "meat_image";


    /**
     * DeskInfo
     */

    public static final String   DESK_INFO="desk_info";
    public static final String   DESK_LOCAL_IP="local_ip";
    public static final String   DESK_LOCAL_DESK="local_desk";

    /**
     * 打印命令
     */
    public static final String CMD_PRINT = "8906063211##";//打印
    public static final String CMD_CLEAR = "8906063212##";//清台
    public static final String CMD_RICE = "8906063213##";//加饭
    public static final String CMD_WATER = "8906063214##";//加水
    public static final String CLEAR_DESK = "clear";//命令
    public static final int BUFFER_SIZE = 1024;


    /**
     * 结账
     */
    public static final String  PAY_TYPE="pay_type";
    public static final String  ALI="ALI";
    public static final String  WEIXIN="WEIXIN";
    public static final  int  PAY_TYEPE_ALI= 1;
    public static final  int  PAY_TYEPE_WX= 2;

    /**
     * 查看详情
     */
    public static final String FOOD_DETAIL  = "food_detail";

    /**
     * Handler执行码
     */
    public static final int send_msg_code0 = 0x100;
    public static final int send_msg_code1 = 0x101;
    public static final int send_msg_code2 = 0x102;
    public static final int send_msg_code3 = 0x103;
    public static final int send_msg_code4 = 0x104;
    public static final int send_msg_code5 = 0x105;
    public static final int send_msg_code6 = 0x106;
    public static final int send_msg_code7 = 0x107;
    public static final int send_msg_code8 = 0x108;
    public static final int send_msg_code9 = 0x109;
    public static final int send_msg_code10 = 0x110;
    public static final int send_msg_code11 = 0x111;
    public static final int send_msg_code12 = 0x112;


}
