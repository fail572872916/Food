package com.food.lmln.food.activity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.food.lmln.food.R;
import com.food.lmln.food.adapter.FoodOrderAdapter;
import com.food.lmln.food.adapter.FoodTypeMenuAdapter;
import com.food.lmln.food.base.SocketBaseActivity;
import com.food.lmln.food.bean.DeskInfo;
import com.food.lmln.food.bean.DeskTemp;
import com.food.lmln.food.bean.MenuButton;
import com.food.lmln.food.bean.OrderInfo;
import com.food.lmln.food.bean.OrderTemp;
import com.food.lmln.food.db.Constants;
import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.db.MysqlDb;
import com.food.lmln.food.db.SqlHelper;
import com.food.lmln.food.fragment.Blank2Fragment;
import com.food.lmln.food.fragment.BlankFragment;
import com.food.lmln.food.fragment.FragmentDialogPay;
import com.food.lmln.food.model.OrderTempDao;
import com.food.lmln.food.model.OrderTempImpl;
import com.food.lmln.food.services.SocketService;
import com.food.lmln.food.utils.FileUtils;
import com.food.lmln.food.utils.JsonUtils;
import com.food.lmln.food.utils.NetWorkCheck;
import com.food.lmln.food.utils.VeDate;
import com.food.lmln.food.view.DialogTable;
import com.food.lmln.food.view.MyPopWindow;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.food.lmln.food.db.Constants.DESK_TEMP;
import static com.food.lmln.food.db.Constants.ORDERTABLE;
import static com.food.lmln.food.db.Constants.PASSWORD;
import static com.food.lmln.food.db.Constants.SQLURL;
import static com.food.lmln.food.db.Constants.USERNAME;
import static com.food.lmln.food.utils.OrderUtils.getOrderId;

/**
 * @author Weli
 * @time 2017-11-14  13:05
 * @describe
 */
public class MainActivity extends SocketBaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    ListView mLvLeft;
    ListView mLvRight;
    TextView mTvTitleRight;
    Button mBtAccount;
    Button mBtClear;
    Button mBtWaterAdd;
    Button mBtRiceAdd;
    Button mBtOrderDown;
    TextView mTvMenu;
    TextView mTvPrice;
    TextView mTvMontyCount;
    FoodOrderAdapter mAdapterRight;
    FrameLayout myContent;
    private FloatingActionMenu fab;
    private FloatingActionButton mFabMachine;
    private FloatingActionButton mFabSetting;
    private FloatingActionButton mFabSell;
    private SQLiteDatabase db;
    private OrderTempDao dao;
    SqlHelper helper;
    private MyThread myThread;
    private Thread thread;
    /**
     * 用于对Fragment进行管理
     */
    private BlankFragment fragment1;
    private Blank2Fragment fragment2;
    FragmentDialogPay editNameDialog;
    private DbManger dbManager;
    private List<OrderInfo> newList = new ArrayList<>();
    private List<OrderInfo> addList = new ArrayList<>();
    private List<OrderInfo> listOrder = new ArrayList<>();
    private List<MenuButton> menuLeft = new ArrayList<>();
    private List<DeskTemp> deskList = new ArrayList<>();
    private String timeNow;
    private String dateNow;
    private String orderNowNo;
    private String deskNo = "";
    private String deskIp = "";
    private int stopCode = 2;
    private Connection conn;
    /**
     * 开台一些列操作
     */
    private String founding; //开台状态
    private int startFouding; //是否开台
    private String finallyOrder;// 写入订单
    private String before;  //临时订单
    private int updateFouding; //自增
    private int startDeskNo;//临时台号是否成功
    private int tempOk = 0;//临时台号是否成功
    private int orderOk; //订单插入成功？
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTvTitleRight.setText(R.string.main_title);
            mBtAccount.setText(R.string.settlement);
            mBtWaterAdd.setText(R.string.add_water);
            mBtRiceAdd.setText(R.string.add_rice);
            mTvPrice.setText(R.string.order_menu_sum);
            mTvMontyCount.setText(R.string.order_sum);
            mBtOrderDown.setText(R.string.order_place);
            mBtClear.setText(R.string.delete_order);
            switch (msg.what) {
                case Constants.SEND_MSG_CODE1:
                    Bundle bundle2 = msg.getData();
                    menuLeft = (List<MenuButton>) bundle2.getSerializable("listRight");
                    fragment1 = new BlankFragment();
                    if (menuLeft != null && menuLeft.size() > 0) {
                        String name = menuLeft.get(0).getName();
                        EventBus.getDefault().post(new DeskInfo(name, name));
                        mLvLeft.setAdapter(new FoodTypeMenuAdapter(menuLeft, MainActivity.this));
                        mLvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                setTabSelection(position, String.valueOf(menuLeft.get(position).getName()));
                            }
                        });
                    }
                    break;
                case Constants.SEND_MSG_CODE2:
                    double money = 0.0;
                    mAdapterRight = new FoodOrderAdapter(addList, MainActivity.this);
                    mLvRight.setAdapter(mAdapterRight);
                    for (OrderInfo orderInfo : addList) {
                        money += orderInfo.getCount() * orderInfo.getPrice();
                    }
                    mTvMenu.setText("￥:" + money);
                    break;
                case Constants.SEND_MSG_CODE3:
                    mBtOrderDown.setEnabled(true);


                    mBtOrderDown.setEnabled(true);
                    mBtAccount.setEnabled(true);
                    isFlag(true);
                    break;
                case Constants.SEND_MSG_CODE4:
                    Bundle bundle1 = msg.getData();
                    newList = (List<OrderInfo>) bundle1.getSerializable("List");
                    newList = (List<OrderInfo>) bundle1.getSerializable("List");
                    if (stopCode == 2 && newList !=null) {
                        mAdapterRight = new FoodOrderAdapter(newList, MainActivity.this);
                        mAdapterRight.notifyDataSetChanged();
                        mLvRight.setAdapter(mAdapterRight);
                        mBtOrderDown.setEnabled(true);
                        break;

                    } else {
                        if (newList != null) {
                            newList.clear();
                        }
                    }
                    break;
                case Constants.SEND_MSG_CODE5:
                    if (founding != null) {
                        if (founding.equals(Constants.STATUS_RUN) || founding.equals(Constants.STATUS_RUN)) {
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
                case Constants.SEND_MSG_CODE6:
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
                        dao.updOrderemp(new OrderTemp(""));
                        startTemp();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.tip_fail_desk, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.SEND_MSG_CODE7:
                    if (!"".equals(finallyOrder) || finallyOrder != null) {
                        Log.d("sss", finallyOrder + "");
                        FileUtils.rewriteOrdera(finallyOrder);
                        updateDeskTemp();
                    } else {
                        break;
                    }
                    break;
                case Constants.SEND_MSG_CODE8:
                    if (updateFouding == 1) {
                        addDesk(); //添加桌台
                    } else {
                        Toast.makeText(MainActivity.this, R.string.tip_new_add3, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.SEND_MSG_CODE9:
                    if (startDeskNo == 1) {
                        Toast.makeText(MainActivity.this, R.string.tip_runing_order, Toast.LENGTH_SHORT).show();
                        startFounding();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.tip_new_add2, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.SEND_MSG_CODE10:
                    if (tempOk >= 1) {
                        Toast.makeText(MainActivity.this, +R.string.order_ok_print, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObj = new JSONObject();//创建json格式的数据
                        JSONArray jsonArr = new JSONArray();//json格式的数组
                        try {
                            for (OrderInfo orderInfo : listOrder) {
                                JSONObject jsonObjArr = new JSONObject();
                                jsonObjArr.put("name", orderInfo.getName());
                                jsonObjArr.put("price", String.valueOf(orderInfo.getPrice()));
                                jsonObjArr.put("count", 1);
                                jsonArr.put(jsonObjArr);
                            }
                            jsonObj.put("desk_num_str", deskNo);
                            jsonObj.put("people_num", 0);
                            jsonObj.put("detail_food", jsonArr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject json = JsonUtils.useJosn(true, Constants.CMD_PRINT, jsonObj, "", "");
                        Log.d("json1", "jsonObj:" + json);
                        String uJson = JsonUtils.initSend("", deskIp,json, Constants.ORDER_PRINT, "");
                        Log.d(TAG,"uijson"+uJson);
                        sendPrint(uJson);
                    }
                    break;
                case Constants.SEND_MSG_CODE11:
                    if (deskList != null && deskList.size() > 0) {
                        String orderId = null;
                        double price = 0;
                        for (DeskTemp deskTemp : deskList) {
                            orderId = deskTemp.getConsumptionId();
                            price += deskTemp.getFoodPrice() * deskTemp.getFoodCount();
                        }
                        Gson gson = new Gson();
                        String foodDetail = gson.toJson(deskList);

                        inFragment(orderId, price, foodDetail);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.tip_order_exption, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.SEND_MSG_CODE12:
                    Toast.makeText(MainActivity.this, R.string.tip_set_ip, Toast.LENGTH_SHORT).show();
                    DialogTable.showDialog(MainActivity.this);
                    break;
                default:
                    break;
            }
        }
    };

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
        socketInit();
        startSocket();
        //注册事件
        EventBus.getDefault().register(this);
        initView();
        initData();

    }

    private void initData() {
        helper = DbManger.getInstance(this);
        db = helper.getWritableDatabase();
        dbManager = new DbManger(this);
        dbManager.copyDBFile();
        selectMenu();
        selectIpDesk();
        myThread = new MyThread();
        thread = new Thread(myThread);
        thread.start();
        dao = new OrderTempImpl(new SqlHelper(this));
        fragment1 = new BlankFragment();
        editNameDialog = new FragmentDialogPay();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myContent, fragment1);
        transaction.commit();
        isFlag(true);
        SocketService.HOST = deskIp;
        editNameDialog.setOnDialogListener(new FragmentDialogPay.OnDialogListener() {
            @Override
            public void onDialogClick(String person) {
                JSONObject jsonObject = new JSONObject();
                person = person.replaceAll("\\\\", "");
                Log.d("person", person);
                JSONObject js1;
                js1 = JsonUtils.useJosn(true, Constants.CMD_CLEAR, jsonObject, deskNo, person);
                Log.d("MainActivity", js1.toString());
                dao.updOrderemp(new OrderTemp(""));
                String uJson = JsonUtils.initSend("", "",js1, Constants.ORDER_SETTLE, "");
                sendPrint(uJson);

            }
        });
        DialogTable dialogTable = new DialogTable();
        dialogTable.setCloseListener(new DialogTable.OnDialogCloseListener() {
            @Override
            public void onDialogCloseClick() {
                selectIpDesk();
                if (!TextUtils.isEmpty(deskNo)) {
                    change(deskNo);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        // 注册广播 最好在onResume中注册
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    /**
     * 销毁方法
     * 销毁
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

    /**
     * 临时下单
     */
    private void startTemp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String order = dao.getOrderemp();
                OrderTemp ot = new OrderTemp();
                boolean down = false;
                if (order == null || "".equals(order)) {
                    orderNowNo = getOrderId();
                    ot.setOrder_temp(orderNowNo);
                    dao.updOrderemp(ot);
                    down = true;
                } else {
                    orderNowNo = dao.getOrderemp();
                }
                String sql;
                for (OrderInfo orderInfo : addList) {
                    dateNow = VeDate.getStringDateShort();
                    timeNow = VeDate.getTimeShort();
                    sql = "INSERT INTO " + DESK_TEMP + "(`date`, `time`, `desk_no`, `consumptionID`, `foodName`, `foodPrice`, `foodCount`)" + " VALUES ('" + dateNow + "', '" + timeNow + "', '" + deskNo + "','" + orderNowNo + "', '" + orderInfo.getName() + "', '" + orderInfo.getPrice() + "', " + orderInfo.getCount() + ");";
                    conn = MysqlDb.openConnection(Constants.SQLURL, Constants.USERNAME, Constants.PASSWORD);
                    tempOk = MysqlDb.exuqueteUpdate(conn, sql);
                }
                if (tempOk > 0 && down) {
                    downOrder();
                }
                mHandler.sendEmptyMessage(Constants.SEND_MSG_CODE10);
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
                conn = MysqlDb.openConnection(Constants.SQLURL, Constants.USERNAME, Constants.PASSWORD);
                startDeskNo = MysqlDb.exuqueteUpdate(conn, "insert into " + Constants.DESK_CONSUMPTIONID + " values('" + deskNo + "','" + before + "'," + Constants.TEMP_PEOPLE + ");");
                mHandler.sendEmptyMessage(Constants.SEND_MSG_CODE9);
            }
        }).start();
    }

    /**
     * 更新奔条目i
     */
    private void updateDeskTemp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                before = finallyOrder.substring(0, 9);
                int a = Integer.valueOf(finallyOrder.substring(9, finallyOrder.length()));
                String date = VeDate.getStringDateShort();
                a += 1;
                String and = VeDate.addZeroForNum(String.valueOf(a), 4);
                before = before + String.valueOf(and);
                conn = MysqlDb.openConnection(Constants.SQLURL, Constants.USERNAME, Constants.PASSWORD);
                updateFouding = MysqlDb.exuqueteUpdate(conn, "update    " + Constants.ORDER_TEMP + " set  " + Constants.ORDER_ID + "='" + before + "' , " + Constants.ORDER_DATE + "='" + date + "'");
                mHandler.sendEmptyMessage(Constants.SEND_MSG_CODE8);
            }
        }).start();
    }

    /**
     * 插入订单
     */
    private void downOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql1 = "INSERT INTO " + Constants.ORDER_INFO + "( `order_id`, `desk`, `strat_time`, `end_time`, `order_date`, `order_describe`, `order_price`, `order_status`, `pay_type`)" + " VALUES ('"
                        + orderNowNo + "', '" + deskNo + "', '" + timeNow + "','" + "" + "', '" + dateNow + "', '" + "" + "', '" + "" + "','" + 1 + "','" + 0 + "');";
                conn = MysqlDb.openConnection(Constants.SQLURL, Constants.USERNAME, Constants.PASSWORD);
                orderOk = MysqlDb.exuqueteUpdate(conn, sql1);
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
                conn = MysqlDb.openConnection(Constants.SQLURL, Constants.USERNAME, Constants.PASSWORD);
                finallyOrder = MysqlDb.selectOrderTemp(conn, "select  " + Constants.ORDER_ID + " from  " + Constants.ORDER_TEMP + " ");
                mHandler.sendEmptyMessage(Constants.SEND_MSG_CODE7);
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
                conn = MysqlDb.openConnection(Constants.SQLURL, Constants.USERNAME, Constants.PASSWORD);
                startFouding = MysqlDb.exuqueteUpdate(conn, "update    " + Constants.SHOP_DESK + " set  " + Constants.SHOP_STATUS + "='" + Constants.STATUS_RUN + "'  where " + Constants.DESK_NO + " =" + "'" + deskNo + "'");
                mHandler.sendEmptyMessage(Constants.SEND_MSG_CODE6);
            }
        }).start();
    }

    /**
     * 查询价格
     */
    public void selectOrderMoney() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(Constants.SQLURL, Constants.USERNAME, Constants.PASSWORD);
                deskList = MysqlDb.selectMoney(conn, "select *  from  " +
                        Constants.DESK_TEMP + " where " +
                        Constants.DESK_NO + " =" + "'" + deskNo + "'");
//                Log.d("MainActivity","deskListfdsa"+ deskList);
//                Message msg =new Message();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Constant.DESK_TEMP, (Serializable) sdeskList);
//                msg.setData(bundle);
                mHandler.sendEmptyMessage(Constants.SEND_MSG_CODE11);
            }
        }).start();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        mLvLeft = (ListView) findViewById(R.id.lv_main);
        fab = (FloatingActionMenu) findViewById(R.id.fab);
        myContent = (FrameLayout) findViewById(R.id.myContent);
        mTvMenu = (TextView) findViewById(R.id.tv_order_sum);
        mLvRight = (ListView) findViewById(R.id.lv_main_order);
        mBtClear = (Button) findViewById(R.id.bt_order_clear);
        mBtOrderDown = (Button) findViewById(R.id.bt_order_place);
        mTvTitleRight = (TextView) findViewById(R.id.tv_order_title);
        mTvPrice = (TextView) findViewById(R.id.tv_order_price);
        mFabMachine = (FloatingActionButton) findViewById(R.id.fab_robot);
        mTvMontyCount = (TextView) findViewById(R.id.tv_order_sum_name);
        mFabSetting = (FloatingActionButton) findViewById(R.id.fab_setting);
        mBtRiceAdd = (Button) findViewById(R.id.bt_order_add_rice);
        mBtWaterAdd = (Button) findViewById(R.id.bt_order_add_water);
        mBtRiceAdd.setVisibility(View.GONE);
        mBtWaterAdd.setVisibility(View.GONE);
        mBtAccount = (Button) findViewById(R.id.bt_order_add_settlement);
        mFabSell = (FloatingActionButton) findViewById(R.id.fab_vending_machine);
        initListener();

    }

    private void initListener() {
        fab.setClosedOnTouchOutside(true);
        mFabMachine.setOnClickListener(this);
        mFabSetting.setOnClickListener(this);
        mFabSell.setOnClickListener(this);
        mBtOrderDown.setOnClickListener(clickListener);
        mBtClear.setOnClickListener(clickListener);
//        bt_order_add_water.setOnClickListener(listerner);
//        bt_order_add_rice.setOnClickListener(listerner);
        mBtAccount.setOnClickListener(clickListener);
    }

    /**
     * 查询桌台与Ip
     */
    private void selectIpDesk() {
        db = helper.getWritableDatabase();
        int set = DbManger.getCountPerson(db, Constants.DESK_INFO);
        if (set < 1) {
            Message msg = new Message();
            msg.what = Constants.SEND_MSG_CODE12;
            mHandler.sendMessage(msg);
        } else {
            db = helper.getWritableDatabase();
            List<DeskInfo> li = DbManger.selectDeskInfo(db, Constants.DESK_INFO);
            if (li.size() > 0) {
                deskNo = li.get(0).getLocal_desk();
                deskIp = li.get(0).getLocal_ip();
            }
        }
    }

    /**
     * 查询菜单
     */
    private void selectMenu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                menuLeft = MysqlDb.selectCuisine(conn, "select  * from  " + ORDERTABLE + "");
                Message msg = new Message();
                msg.what = Constants.SEND_MSG_CODE1;
                Bundle bundle = new Bundle();
                bundle.putSerializable("listRight", (Serializable) menuLeft);
                msg.setData(bundle);
                mHandler.sendMessage(msg);

            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_setting:
                DialogTable.showDialog(MainActivity.this);
                mFabSetting.showButtonInMenu(false);
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
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_order_place:
                    if (!socketSend(Constants.SOCKETPARMAR)) {
                        Toast.makeText(MainActivity.this, R.string.socket_seng_check, Toast.LENGTH_SHORT).show();
                    } else if (addList == null || addList.size() < 1) {
                        Toast.makeText(MainActivity.this, R.string.tip_not_order, Toast.LENGTH_SHORT).show();
                    } else if (!NetWorkCheck.isNetworkAvailable(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, +R.string.netrock_check, Toast.LENGTH_SHORT).show();
                    } else if (deskNo == null || "".equals(deskNo)) {
                        selectIpDesk();
                    } else {
                        mBtOrderDown.setEnabled(false);
//                        mHandlerFlag = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                selectIpDesk();
                                conn = MysqlDb.openConnection(Constants.SQLURL, Constants.USERNAME, Constants.PASSWORD);
                                founding = MysqlDb.selectDeskNO(conn, "select  " +
                                        Constants.SHOP_STATUS + " from  " +
                                        Constants.SHOP_DESK + " where " +
                                        Constants.DESK_NO + " =" + "'" + deskNo + "'");
                                mHandler.sendEmptyMessage(Constants.SEND_MSG_CODE5);
                            }
                        }).start();
                    }
                    break;
//                case R.id.bt_order_add_water:
//                    if (!NetWorkCheck.isNetworkAvailable(MainActivity.this)) {
//                        Toast.makeText(MainActivity.this, +R.string.netrock_check, Toast.LENGTH_SHORT).show();
//                    } else if (Socketudge(Constant.SOCKETPARMAR)) {
////                        String rid = JPushInterface.getRegistrationID(getApplicationContext());
//                        JSONObject jsonObject = new JSONObject();
//                        try {
//                            jsonObject.put("desk_no_str", deskNo);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        String json = JsonUtils.useJosn(true, Constant.CMD_WATER, jsonObject, "");
//                        Log.d("MainActivity", json);
//                        Socketudge(json);
//                    }
//                    break;
//                case R.id.bt_order_add_rice:
//                    if (!NetWorkCheck.isNetworkAvailable(MainActivity.this)) {
//                        Toast.makeText(MainActivity.this, +R.string.netrock_check, Toast.LENGTH_SHORT).show();
//                    } else if (Socketudge(Constant.SOCKETPARMAR)) {
//                        JSONObject jsonObject1 = new JSONObject();
//                        try {
//                            jsonObject1.put("desk_no_str", deskNo);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        String json1 = JsonUtils.useJosn(true, Constant.CMD_RICE, jsonObject1, "");
//                        Log.d("gg", json1);
//                        Socketudge(json1);
//                    }
//                    break;
                case R.id.bt_order_add_settlement:
                    if (!NetWorkCheck.isNetworkAvailable(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, +R.string.netrock_check, Toast.LENGTH_SHORT).show();
                    } else if (socketSend(Constants.SOCKETPARMAR)) {
//                        bt_order_add_settlement.setEnabled(false);
                        isFlag(true);
                        selectOrderMoney();
                    }
                    break;
                case R.id.bt_order_clear:
                    addList.clear();
                    newList.clear();
                    listOrder.clear();
                    mAdapterRight.notifyDataSetChanged();
                    Message msg = new Message();
                    msg.what = Constants.SEND_MSG_CODE1;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("listRight", (Serializable) menuLeft);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                    isFlag(true);
                    mTvMenu.setText("");
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 执行定时查询
     */
    public class MyThread extends Thread {
        private final Object lock = new Object();
        private boolean pause = false;

        /**
         * 调用这个方法实现暂停线程
         */
        void pauseThread() {
            pause = true;
        }

        /**
         * 调用这个方法实现恢复线程的运行
         */
        void resumeThread() {
            pause = false;
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        /**
         * 注意：这个方法只能在run方法里调用，不然会阻塞主线程，导致页面无响应
         */
        void onPause() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                while (true) {
                    // 让线程处于暂停等待状态
                    while (pause) {
                        onPause();
                    }
                    try {
                        conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                        newList = MysqlDb.selectRiht(conn, "select  * from   desk_temp where desk_no='" + deskNo + "'");
                        Message message = new Message();
                        message.what = Constants.SEND_MSG_CODE4;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("List", (Serializable) newList);
                        bundle.putInt("Select", 2);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                        ++index;
                        // 线程暂停10秒，单位毫秒
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        //捕获到异常之后，执行break跳出循环
                        break;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打印数据
     */
    private void sendPrint(String jsonObj) {
        if (socketSend(Constants.SOCKETPARMAR)) {
            socketSend(jsonObj);
            addList.clear();
            listOrder.clear();
            stopCode = 2;
            Message msg = new Message();
            msg.what = Constants.SEND_MSG_CODE3;
            mHandler.sendMessage(msg);
        } else {
            Message msg1 = new Message();
            msg1.what = Constants.SEND_MSG_CODE3;
            stopCode = 2;
            mHandler.sendMessage(msg1);
            Toast.makeText(MainActivity.this, "连接失败...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 进入选中的Fragment
     *
     * @param index     数量
     * @param tableName 表名
     */
    @SuppressLint("NewApi")
    private void setTabSelection(int index, String tableName) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        Bundle bundle2 = new Bundle();
        hideFragments(transaction);
        int num = ++index;
        if (num % 2 == 0) {
            if (fragment2 == null) {
                // 如果NewsFragment为空，则创建一个并添加到界面上
                fragment2 = new Blank2Fragment();
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
                //如果需要，添加到back栈中
                fragmentTransaction.commit();
                fragment2 = new Blank2Fragment();
                bundle2.putString("foodName", tableName);
                fragment2.setArguments(bundle2);
                transaction.add(R.id.myContent, fragment2);
            }
        } else {
            if (fragment1 == null) {
                // 如果SettingFragment为空，则创建一个并添加到界面上
                fragment1 = new BlankFragment();

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
                //如果需要，添加到back栈中
                fragmentTransaction.commit();
                Log.d("MainActivity", tableName);
                fragment1 = new BlankFragment();
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
    }

    private int index = 1;
    private int count = 1;

    /**
     * even bus
     *
     * @param info 得到的信息
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMoonEvent(OrderInfo info) {
        info = new OrderInfo(index, info.getName(), info.getPrice(), count, info.isFlag());
        if (info.isFlag()) {
            isFlag(false);
        }
        listOrder.add(info);
        addList = new ArrayList<>();
        Iterator<OrderInfo> it = listOrder.iterator();
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
                    d.setId(dt.getId()  +1);
                }
            }
            if (!flag) {
                addList.add(d);
                d.setCount(1);
            }
        }
        mHandler.sendEmptyMessage(Constants.SEND_MSG_CODE2);
    }


    /**
     * 判断停止线程
     *
     * @param flag t/f
     */
    public void isFlag(boolean flag) {
        if (flag) {
            stopCode = 2;
            myThread.resumeThread();
        } else {
            stopCode = 1;
            myThread.pauseThread();
        }
    }


    /**
     * 进入Fragment
     */
    private void inFragment(final String orderNo, final double money, final String list) {
        final MyPopWindow p = new MyPopWindow(MainActivity.this);
        p.showAtLocation(MainActivity.this.findViewById(R.id.myContent), Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        p.setOnItemClickListener(new MyPopWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                switch (v.getId()) {
                    case R.id.im_pay_ali:
                        bundle.putString(Constants.PAY_TYPE, Constants.ALI + "####" + orderNo + "####" + String.valueOf(money) + "####" + list);
                        editNameDialog.setArguments(bundle);
                        editNameDialog.show(fm, "payDialog");
                        p.dismiss();
                        break;
                    case R.id.im_pay_weixin:
                        bundle.putString(Constants.PAY_TYPE, Constants.WEIXIN + "####" + orderNo + "####" + String.valueOf(money) + "####" + list);
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

    private void socketInit() {
        //给全局消息接收器赋值，并进行消息处理
        mReciver = new BaseMessageBackReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(SocketService.HEART_BEAT_ACTION)) {
                    Log.d(TAG, "心跳");
                } else {
                    String message = intent.getStringExtra("message");
                    String code = intent.getStringExtra("code");
                    Log.d(TAG, code);
                    if (code.equals(Constants.SOCKET_MSG_CAR_INFO)) {

                    }
                }
            }
        };


    }


}
