package com.food.lmln.food.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.food.lmln.food.R;
import com.food.lmln.food.adapter.FoodOrderAdapter;
import com.food.lmln.food.adapter.FoodTypeMenuAdapter;
import com.food.lmln.food.bean.DeskInfo;
import com.food.lmln.food.bean.MenuButton;
import com.food.lmln.food.bean.OrderInfo;
import com.food.lmln.food.db.Constant;
import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.db.MysqlDb;
import com.food.lmln.food.db.SqlHelper;
import com.food.lmln.food.fragment.Blank2Fragment;
import com.food.lmln.food.fragment.Blank3Fragment;
import com.food.lmln.food.fragment.BlankFragment;
import com.food.lmln.food.fragment.FragmentDialogPay;
import com.food.lmln.food.utils.FileUtils;
import com.food.lmln.food.utils.JsonUtils;
import com.food.lmln.food.utils.VeDate;
import com.food.lmln.food.utils.socket_client;
import com.food.lmln.food.view.DialogTablde;
import com.food.lmln.food.view.MyPopWindow;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cn.jpush.android.api.JPushInterface;
import static com.food.lmln.food.db.Constant.DESK_TEMP;
import static com.food.lmln.food.db.Constant.ORDERTABLE;
import static com.food.lmln.food.db.Constant.PASSWORD;
import static com.food.lmln.food.db.Constant.SQLURL;
import static com.food.lmln.food.db.Constant.USERNAME;
import static com.food.lmln.food.db.Constant.send_msg_code1;
import static com.food.lmln.food.db.Constant.send_msg_code2;
import static com.food.lmln.food.db.Constant.send_msg_code3;
import static com.food.lmln.food.db.Constant.send_msg_code4;
import static com.food.lmln.food.db.Constant.send_msg_code5;
import static com.food.lmln.food.utils.OrderUtils.getOrderId;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 布局1
     * 布局2
     * 布局3
     */
    LinearLayout lin_one;
    LinearLayout lin_three;
    ListView lv_main; //左边订单
    ListView lv_main_order;  //右边订单
    TextView tv_order_title;  //标题
    Button bt_order_add_settlement;     //结算
    Button bt_order_add_water;          //加水
    Button bt_order_add_rice;           //加水
    Button bt_order_place;                //下/改单
    TextView tv_order_sum;              //菜单价格
    TextView tv_order_price;            //  200
    TextView tv_order_sum_name;         //总计
    FoodOrderAdapter mAdapter_order; //l类型适配器
    //创建Socket通信
    static socket_client client = new socket_client();
    FrameLayout myContent;
    private SQLiteDatabase db;
    SqlHelper helper;
    /**
     * 用于对Fragment进行管理
     */
    private BlankFragment fragment1;
    private Blank2Fragment fragment2;
    private Blank3Fragment fragment3;
    FragmentDialogPay editNameDialog;
    private DbManger dbManager;
    private List<OrderInfo> newList = new ArrayList<>();
    private List<OrderInfo> addList = new ArrayList<>();
    private List<OrderInfo> list_order = new ArrayList<>();
    private List<MenuButton> listRight = new ArrayList<MenuButton>();
    private FloatingActionMenu fab;  //悬浮菜单按钮
    private FloatingActionButton fab_robot;  //呼叫机器人
    private FloatingActionButton fab_setting; //设置
    private FloatingActionButton fab_vending_machine; //售卖机
    public boolean mHandlerFlag = true;
    private String timeNow;//当前时间
    private String dateNow; //当前日期
    private String orderNowNo;//当前订单编号
    private String deskNo = ""; //当前桌台号
    private String deskIp = ""; //连接ip
    private int stopCode = 2;
    private Connection conn; //Connection连接
    /**
     * 开台一些列操作
     */
    private String founding; //开台状态
    private int startFouding; //是否开台
    private String finallyOrder;// 写入订单
    private String before;  //临时订单
    private int updateFouding; //自增
    private int startDeskNo;//临时台号是否成功
    private int tempOk=0;//临时台号是否成功
    private int orderOk;//临时台号是否成功
    private String orderNo = null; //查询当前桌台订单号
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_order_title.setText(R.string.main_title);
            bt_order_add_settlement.setText(R.string.settlement);
            bt_order_add_water.setText(R.string.add_water);
            bt_order_add_rice.setText(R.string.add_rice);
            tv_order_price.setText(R.string.order_menu_sum);
            tv_order_sum_name.setText(R.string.order_sum);
            bt_order_place.setText(R.string.order_place);
            switch (msg.what) {
                case send_msg_code1:
                    Bundle bundle2 = msg.getData();
                    listRight = (List<MenuButton>) bundle2.getSerializable("listRight");
                    fragment1 = new BlankFragment();
                    if (listRight.size() > 0) {
                        String name = listRight.get(0).getName();
                        EventBus.getDefault().post(new DeskInfo(name, name));
                        lv_main.setAdapter(new FoodTypeMenuAdapter(listRight, MainActivity.this));
                        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                setTabSelection(position, String.valueOf(listRight.get(position).getName()));
                            }
                        });
                    }
                    break;
                case send_msg_code2:
                    double money = 0.0;
                    mAdapter_order = new FoodOrderAdapter(addList, MainActivity.this);
                    lv_main_order.setAdapter(mAdapter_order);
                    for (OrderInfo orderInfo : addList) {
                        money += orderInfo.getCount() * orderInfo.getPrice();
                    }
                    tv_order_sum.setText("￥:" + money);
                    break;
                case send_msg_code3:
                    Bundle bundle = msg.getData();
                    int num = bundle.getInt("upOrder");
                    bt_order_place.setEnabled(num >= 1 ? false : true);
                    isFlag(true);
                    break;
                case send_msg_code4:
                    Bundle bundle1 = msg.getData();
                    int num1 = bundle1.getInt("Select");
                    newList = (List<OrderInfo>) bundle1.getSerializable("List");
                    Log.d("MainActivity", "newList:" + newList);
                    if (stopCode == 2) {
                        if (num1 > 1 &&  newList != null) {
                            mAdapter_order = new FoodOrderAdapter(newList, MainActivity.this);
                            mAdapter_order.notifyDataSetChanged();
                            lv_main_order.setAdapter(mAdapter_order);
                            bt_order_place.setEnabled(true);
                            break;
                        }
                    } else {
                        if (newList!=null&&newList.size()>0);
                        newList.clear();
                    }
                    break;
                case send_msg_code5:
                    if (founding != null) {
                        if (founding == Constant.STATUS_RUN || founding.equals(Constant.STATUS_RUN)) {
                            startTemp();
                        } else {
                            //查询桌台与
//                            selectIpDesk();
                            selectAndAdd();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, R.string.tip_check_desk, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constant.send_msg_code6:
                    if (startFouding == 1) {
                        Toast.makeText(MainActivity.this, R.string.tip_succeed_desk, Toast.LENGTH_SHORT).show();
//                    JSONObject jsonObject   = new JSONObject();
//                        try {
//                            jsonObject.put("desk_num_str",deskNo);
//                            jsonObject.put("desk_ip_str",deskIp);
//                            jsonObject.put("desk_status","open");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        String json =JsonUtils.useJosn(true, Constant.CMD_PRINT, jsonObject);
//                        sendPrint(json);
                        startTemp();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.tip_fail_desk, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constant.send_msg_code7:
                    if (!finallyOrder.equals("") || finallyOrder != null) {
                        FileUtils.rewriteOrdera(finallyOrder);
                        updateDeskTemp();
                    } else {
                        break;
                    }
                    break;
                case Constant.send_msg_code8:
                    if (updateFouding == 1) {
                        addDesk(); //添加桌台
                    } else {
                        Toast.makeText(MainActivity.this, R.string.tip_new_add3, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constant.send_msg_code9:
                    if (startDeskNo == 1) {
                        Toast.makeText(MainActivity.this, R.string.tip_runing_order, Toast.LENGTH_SHORT).show();
                        startFounding();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.tip_new_add2, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constant.send_msg_code10:
                    if (orderOk == 1) {
                        Toast.makeText(MainActivity.this, + R.string.order_ok_print, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObj = new JSONObject();//创建json格式的数据
                        JSONArray jsonArr = new JSONArray();//json格式的数组
                        try {
                            for (OrderInfo orderInfo : list_order) {
                                JSONObject jsonObjArr = new JSONObject();
                                jsonObjArr.put("name", orderInfo.getName());
                                jsonObjArr.put("price", String.valueOf(orderInfo.getPrice()));
                                jsonObjArr.put("count", 1);
                                jsonArr.put(jsonObjArr);//将json格式的数据放到json格式的数组里
                            }
                            jsonObj.put("desk_num_str", deskNo);
                            jsonObj.put("people_num", 0);
                            jsonObj.put("detail_food", jsonArr);//再将这个json格式的的数组放到最终的json对象中。
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                     String json =JsonUtils.useJosn(true, Constant.CMD_PRINT, jsonObj,null);
                        Log.d("json1", "jsonObj:" + json);
                        sendPrint(json);
                    }
                    break;
                case Constant.send_msg_code11:
                    if (orderNo != null && orderNo != "")
                        inFragment(orderNo);
                    else
                        Toast.makeText(MainActivity.this, R.string.tip_is_start, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 临时下单
     */
    private void startTemp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                orderNowNo = getOrderId();
                String sql = "";
                for (OrderInfo orderInfo : addList) {
                    dateNow = VeDate.getStringDateShort();
                    timeNow = VeDate.getTimeShort();
                    sql = "INSERT INTO " + DESK_TEMP + "(`date`, `time`, `desk_no`, `consumptionID`, `foodName`, `foodPrice`, `foodCount`)" + " VALUES ('" + dateNow + "', '" + timeNow + "', '" + deskNo + "','" + orderNowNo + "', '" + orderInfo.getName() + "', '" + orderInfo.getPrice() + "', " + orderInfo.getCount() + ");";
                    conn = MysqlDb.openConnection(Constant.SQLURL, Constant.USERNAME, Constant.PASSWORD);
                    tempOk = MysqlDb.exuqueteUpdate(conn, sql);
                }
                if (tempOk>0) {
                    if (tempOk == 1) {
                        String sql1 = "INSERT INTO " + Constant.ORDER_INFO + "( `order_id`, `desk`, `strat_time`, `end_time`, `order_date`, `order_describe`, `order_price`, `order_status`, `pay_type`)" + " VALUES ('"
                                + orderNowNo + "', '" + deskNo + "', '" + timeNow + "','" + "" + "', '" + dateNow + "', '" + "" + "', '" + "20" + "','" + 1 + "','" + 0 + "');";
                        conn = MysqlDb.openConnection(Constant.SQLURL, Constant.USERNAME, Constant.PASSWORD);
                        orderOk = MysqlDb.exuqueteUpdate(conn, sql1);
                    }
                }
                mHandler.sendEmptyMessage(Constant.send_msg_code10);
            }
        }).start();
    }

    /**
     * 新开的临时台
     */
    private void addDesk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(Constant.SQLURL, Constant.USERNAME, Constant.PASSWORD);
                startDeskNo = MysqlDb.exuqueteUpdate(conn, "insert into " + Constant.DESK_CONSUMPTIONID + " values('" + deskNo + "','" + before + "'," + Constant.TEMP_PEOPLE + ");");
                mHandler.sendEmptyMessage(Constant.send_msg_code9);
            }
        }).start();
    }

    /*
         * 更新奔条目i
         */
    private void updateDeskTemp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                before = finallyOrder.substring(0, 7);
                int a = Integer.valueOf(finallyOrder.substring(7, finallyOrder.length()));
                String date = VeDate.getStringDateShort();
                a = a + 1;
                before = before + String.valueOf(a);
                conn = MysqlDb.openConnection(Constant.SQLURL, Constant.USERNAME, Constant.PASSWORD);
                updateFouding = MysqlDb.exuqueteUpdate(conn, "update    " + Constant.ORDER_TEMP + " set  " + Constant.ORDER_ID + "='" + before + "' , " + Constant.ORDER_DATE + "='" + date + "'");
                mHandler.sendEmptyMessage(Constant.send_msg_code8);
            }
        }).start();
    }
    /**
     * 添加桌台
     */
    private void selectAndAdd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(Constant.SQLURL, Constant.USERNAME, Constant.PASSWORD);
                finallyOrder = MysqlDb.selectOrderTemp(conn, "select  " + Constant.ORDER_ID + " from  " + Constant.ORDER_TEMP + " ");
                mHandler.sendEmptyMessage(Constant.send_msg_code7);
            }
        }).start();
    }
    /**
     * 进行开台
     */
    public void startFounding() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(Constant.SQLURL, Constant.USERNAME, Constant.PASSWORD);
                startFouding = MysqlDb.exuqueteUpdate(conn, "update    " + Constant.SHOP_DESK + " set  " + Constant.SHOP_STATUS + "='" + Constant.STATUS_RUN + "'  where " + Constant.DESK_NO + " =" + "'" + deskNo + "'");
                mHandler.sendEmptyMessage(Constant.send_msg_code6);
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //注册事件
        EventBus.getDefault().register(this);

        initView();
        initData();
        new Thread(new MyThread()).start();

    }

    /**
     * 初始化
     */
    private void initSokect() {
        if (deskIp.isEmpty() && deskNo.isEmpty()) {
            System.out.println("主机信息为空，请补充后再试试");
        } else {
            // 连线 server
            client.runclient(deskIp);
            //此线程为接收菜单的线程，循环接收，
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        if (client.i) {

                            client.i = false;
                        } else if (client.j) {

                            client.j = false;
                        }
                    }
                }
            }.start();
        }
    }

    /**
     * 初始化组件
     */
    private void initView() {
        lv_main = (ListView) findViewById(R.id.lv_main);
        fab = (FloatingActionMenu) findViewById(R.id.fab);
        lin_one = (LinearLayout) findViewById(R.id.lin_one);
        myContent = (FrameLayout) findViewById(R.id.myContent);
        lin_three = (LinearLayout) findViewById(R.id.lin_three);
        tv_order_sum = (TextView) findViewById(R.id.tv_order_sum);
        lv_main_order = (ListView) findViewById(R.id.lv_main_order);
        bt_order_place = (Button) findViewById(R.id.bt_order_place);
        tv_order_title = (TextView) findViewById(R.id.tv_order_title);
        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        fab_robot = (FloatingActionButton) findViewById(R.id.fab_robot);
        bt_order_add_rice = (Button) findViewById(R.id.bt_order_add_rice);
        tv_order_sum_name = (TextView) findViewById(R.id.tv_order_sum_name);
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        bt_order_add_water = (Button) findViewById(R.id.bt_order_add_water);
        bt_order_add_settlement = (Button) findViewById(R.id.bt_order_add_settlement);
        fab_vending_machine = (FloatingActionButton) findViewById(R.id.fab_vending_machine);
        fab.setClosedOnTouchOutside(true);
        fab_robot.setOnClickListener(this);
        fab_setting.setOnClickListener(this);
        fab_vending_machine.setOnClickListener(this);
        bt_order_place.setOnClickListener(listerner);
        bt_order_add_water.setOnClickListener(listerner);
        bt_order_add_settlement.setOnClickListener(listerner);
        fragment1 = new BlankFragment();
        editNameDialog = new FragmentDialogPay();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myContent, fragment1);
        transaction.commit();
        helper = DbManger.getInstance(this);
        db = helper.getWritableDatabase();
        dbManager = new DbManger(this);
        dbManager.copyDBFile();
        selectIpDesk();
        initSokect();
        editNameDialog.setOnDialogListener(new FragmentDialogPay.OnDialogListener() {
            @Override
            public void onDialogClick(String person) {
                Log.d("person", person);
                JSONObject js =null;
                String js1=null;
                try {
                     js = new JSONObject(person);
                    if(js!= null){
                   js1=      JsonUtils.useJosn(true,Constant.CMD_CLEAR,js,deskNo);
                        Log.d("ks1", js1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(js1!=null)
                sendPrint(js1);
            }
        });
    }
    /**
     * 查询菜单
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                listRight = MysqlDb.selectCuisine(conn, "select  * from  " + ORDERTABLE + "");
                Message msg = new Message();
                msg.what = send_msg_code1;
                Bundle bundle = new Bundle();
                bundle.putSerializable("listRight", (Serializable) listRight);
                msg.setData(bundle);
                mHandler.sendMessage(msg);

            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_setting:
                 DialogTablde.showDialog(MainActivity.this);
                fab_setting.showButtonInMenu(false);
                break;
            case R.id.fab_robot:
                Toast.makeText(this, "呼叫机器人", Toast.LENGTH_SHORT).show();
                break;

            case R.id.fab_vending_machine:
                Toast.makeText(this, "呼叫售卖机", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        fab.toggle(false);
    }

    /**
     * button clicListener
     */
    View.OnClickListener listerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_order_place:
                    selectIpDesk();
                    if (addList == null || addList.size() < 1) {
                        Toast.makeText(MainActivity.this, R.string.tip_not_order, Toast.LENGTH_SHORT).show();
                    } else {
                        mHandlerFlag = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                selectIpDesk();
                                conn = MysqlDb.openConnection(Constant.SQLURL, Constant.USERNAME, Constant.PASSWORD);
                                founding = MysqlDb.selectDeskNO(conn, "select  " + Constant.SHOP_STATUS + " from  " + Constant.SHOP_DESK + " where " + Constant.DESK_NO + " =" + "'" + deskNo + "'");
                                mHandler.sendEmptyMessage(Constant.send_msg_code5);
                            }
                        }).start();
                    }
                    break;
                case R.id.bt_order_add_water:
                    String rid = JPushInterface.getRegistrationID(getApplicationContext());
                    if (!rid.isEmpty()) {
                        Toast.makeText(MainActivity.this, rid, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.bt_order_add_rice:
                    JPushInterface.init(getApplicationContext());
                    break;
                case R.id.bt_order_add_settlement:
                    inFragment(VeDate.getOrderNum());
//                    if(deskNo.isEmpty())
//                    selectIpDesk();
//                    else
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            conn = MysqlDb.openConnection(Constant.SQLURL, Constant.USERNAME, Constant.PASSWORD);
//                            orderNo = MysqlDb.selectDeskNO(conn, "select  " + Constant.CONSUMPTIONID + " from  " + Constant.DESK_CONSUMPTIONID + " where " + Constant.DESK_NO + " =" + "'" + deskNo + "'");
//                            mHandler.sendEmptyMessage(Constant.send_msg_code11);
//                        }
//                    }).start();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 定时运行
     */
    public class MyThread implements Runnable {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        public void stop() {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            try {
                while (mHandlerFlag) {
                    Thread.sleep(3000);// 线程暂停10秒，单位毫秒
                    conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                    newList = MysqlDb.selectRiht(conn, "select  * from   desk_temp where desk_no='" + deskNo + "'");
                    Message message = new Message();
                    message.what = send_msg_code4;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("List", (Serializable) newList);
                    bundle.putInt("Select", 2);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 查询桌台与Ip
     */
    private void selectIpDesk() {
        db = helper.getWritableDatabase();
        int set = DbManger.getCountPerson(db, Constant.DESK_INFO);
        if (set < 1) {

            Toast.makeText(MainActivity.this, R.string.tip_set_ip, Toast.LENGTH_SHORT).show();
            DialogTablde.showDialog(MainActivity.this);
            Log.d("MainActivity", "dsa");
        } else {
            db = helper.getWritableDatabase();
            List<DeskInfo> li = DbManger.selectDeskInfo(db, Constant.DESK_INFO);
            if (li.size() > 0) {
                deskNo = li.get(0).getLocal_desk();
                deskIp = li.get(0).getLocal_ip();
            }
        }
    }
    /**
     * 打印数据
     */
    private void sendPrint(String jsonObj) {
        if (testSocket()) {
            MainActivity.client.SendJson(jsonObj);
            addList = null;
            list_order.clear();
            Message msg = new Message();
            msg.what = send_msg_code3;
            Bundle bundle = new Bundle();
            bundle.putInt("upOrder", 0);
            msg.setData(bundle);
            stopCode = 2;
            mHandler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = send_msg_code3;
            Bundle bundle = new Bundle();
            bundle.putInt("upOrder", 0);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
            Toast.makeText(MainActivity.this, "连接失败...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否断开
     * true  断开
     */
    public boolean testSocket() {
        boolean isConn = MainActivity.client.isServerClose();//判断是否断开
        if (isConn == true) {
            MainActivity.client.again_connect(deskIp);
            } else {
                isConn=false;
            }
         return  isConn;
        }
    /**
     * 进入选中的Fragment
     *
     * @param index
     * @param tableName
     */
    @SuppressLint("NewApi")
    private void setTabSelection(int index, String tableName) {
        Log.d("MainActivity", tableName);
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        int num = ++index;
        if (num % 2 == 0) {
            if (fragment2 == null) {
                // 如果NewsFragment为空，则创建一个并添加到界面上
                fragment2 = new Blank2Fragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("foodName", tableName);
                fragment2.setArguments(bundle2);
                transaction.add(R.id.myContent, fragment2);
            } else {
                // 如果NewsFragment不为空，则直接将它显示出来
                FragmentManager fgManager = getSupportFragmentManager();
                //Activity用来管理它包含的Frament，通过getFramentManager()获取
                FragmentTransaction fragmentTransaction = fgManager.beginTransaction();
                //获取Framgent事务
                Fragment fragment = fgManager.findFragmentById(R.id.myContent);
                //删除一个Fragment之前，先通过FragmentManager的findFragmemtById()，找到对应的Fragment
                fragmentTransaction.remove(fragment);
                //删除获取到的Fragment
                //指定动画，可以自己添加
                String tag = null;
                fragmentTransaction.addToBackStack(tag);
                //如果需要，添加到back栈中
                fragmentTransaction.commit();
                fragment2 = new Blank2Fragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("foodName", tableName);
                fragment2.setArguments(bundle2);
                transaction.add(R.id.myContent, fragment2);
            }
        } else {
            if (fragment1 == null) {
                // 如果SettingFragment为空，则创建一个并添加到界面上
                fragment1 = new BlankFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("foodName", tableName);
                fragment1.setArguments(bundle2);
                transaction.add(R.id.myContent, fragment1);
            } else {
                // 如果NewsFragment不为空，则直接将它显示出来
                FragmentManager fgManager = getSupportFragmentManager();
                //Activity用来管理它包含的Frament，通过getFramentManager()获取
                FragmentTransaction fragmentTransaction = fgManager.beginTransaction();
                //获取Framgent事务
                Fragment fragment = fgManager.findFragmentById(R.id.myContent);
                //删除一个Fragment之前，先通过FragmentManager的findFragmemtById()，找到对应的Fragment
                fragmentTransaction.remove(fragment);
                //删除获取到的Fragment
                //指定动画，可以自己添加
                String tag = null;
                fragmentTransaction.addToBackStack(tag);
                //如果需要，添加到back栈中
                fragmentTransaction.commit();
                Log.d("MainActivity", tableName);
                fragment1 = new BlankFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("foodName", tableName);
                fragment1.setArguments(bundle2);
                transaction.add(R.id.myContent, fragment1);
            }
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        if (fragment1 != null) {
            transaction.hide(fragment1);
        }
        if (fragment2 != null) {
            transaction.hide(fragment2);
        }
        if (fragment3 != null) {
            transaction.hide(fragment3);
        }
    }
    private int index = 1;
    private int count = 1;

    /**
     * 接收传过来的值
     *
     * @param info
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMoonEvent(OrderInfo info) {
        info = new OrderInfo(index, info.getName(), info.getPrice(), count, info.isFlag());
//        info = new OrderInfo();

        Log.d("ss", "info:" + info);
        if (info.isFlag()) {

            isFlag(false);
        }
//        new Thread(new MyThread()).interrupt();
        list_order.add(info);
        addList = new ArrayList<OrderInfo>();
        Iterator<OrderInfo> it = list_order.iterator();
        while (it.hasNext()) {
            OrderInfo d = it.next();
            Iterator<OrderInfo> temp = addList.iterator();
            boolean flag = false;
            while (temp.hasNext()) {
                OrderInfo dt = temp.next();
                if (d.getName().equals(dt.getName()) && d.getPrice() == (dt.getPrice())) {
                    dt.setCount(dt.getCount() + 1);
                    flag = true;
                    break;
                } else {
                    d.setId(dt.getId() + 1);
                }
            }
            if (flag == false) {
                d.setCount(1);
            }
            addList.add(d);
        }
        mHandler.sendEmptyMessage(send_msg_code2);
    }
    /**
     * \
     * 销毁方法
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.finish();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onResume() {
        stopCode = 2;
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
    @Override
    protected void onPostResume() {
        Log.d("MainActivity", "onPostResume");
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        Log.d("MainActivity", "onPause");
        stopCode = 1;
        isFlag(false);
        super.onPause();
    }
    /**
     * 判断停止线程
     *
     * @param falg
     */
    public void isFlag(boolean falg) {
        mHandlerFlag = falg;
        if (mHandlerFlag == true) {
            stopCode=2;
            new Thread(new MyThread()).start();
        } else {
            stopCode = 1;
            MyThread callable = new MyThread();
            Thread th = new Thread(callable);
            th.interrupt();
            callable.stop();
            mHandler.removeCallbacks(new MyThread());
        }
    }

    /**
     * 隐藏状态栏
     */
    private void hideNavigationBar() {
        final View decorView = getWindow().getDecorView();
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(flags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            }
        });
    }

    /**
     * 进入Fragment
     */
    private void inFragment(final String order_no) {
        final String orderNo = order_no;
        final MyPopWindow p = new MyPopWindow(MainActivity.this);
        p.showAtLocation(MainActivity.this.findViewById(R.id.myContent), Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        p.setOnItemClickListener(new MyPopWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                FragmentManager fm = getSupportFragmentManager();

                Bundle bundle = new Bundle();
                switch (v.getId()) {
                    case R.id.im_pay_ali:
                        bundle.putString(Constant.PAY_TYPE, Constant.ALI + "####" + orderNo);
                        editNameDialog.setArguments(bundle);
                        editNameDialog.show(fm, "payDialog");
                        p.dismiss();
                        break;
                    case R.id.im_pay_weixin:
                        bundle.putString(Constant.PAY_TYPE, Constant.WEIXIN + "####" + order_no);
                        editNameDialog.setArguments(bundle);
                        editNameDialog.show(fm, "payDialog");
                        p.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
        }
    }
}