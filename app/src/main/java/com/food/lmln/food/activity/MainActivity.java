package com.food.lmln.food.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
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
import com.food.lmln.food.bean.MenuButton;
import com.food.lmln.food.bean.OrderInfo;

import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.fragment.Blank1Fragment;
import com.food.lmln.food.fragment.Blank2Fragment;
import com.food.lmln.food.fragment.Blank3Fragment;
import com.food.lmln.food.fragment.Blank4Fragment;
import com.food.lmln.food.fragment.Blank5Fragment;

import com.food.lmln.food.utils.VeDate;
import com.food.lmln.food.view.DialogTablde;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.food.lmln.food.db.Constant.CONSUMPTIONID;
import static com.food.lmln.food.db.Constant.DESKTEMP_TIME;
import static com.food.lmln.food.db.Constant.DESK_TEMP;
import static com.food.lmln.food.db.Constant.DSK_NO;
import static com.food.lmln.food.db.Constant.send_msg_code1;
import static com.food.lmln.food.db.Constant.send_msg_code2;
import static com.food.lmln.food.db.Constant.send_msg_code3;
import static com.food.lmln.food.db.Constant.send_msg_code4;
import static com.food.lmln.food.utils.FileUtils.rewriteOrdera;
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
    List<MenuButton> list;  //数据
    FoodOrderAdapter mAdapter_order; //l类型适配器
    List<OrderInfo> list_order;  //数据
    FrameLayout myContent;
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    private Blank1Fragment fragment1;
    private Blank2Fragment fragment2;
    private Blank3Fragment fragment3;
    private Blank4Fragment fragment4;
    private Blank5Fragment fragment5;
    private DbManger dbManager;
    private List<OrderInfo> newList =new ArrayList<>();
    private FloatingActionMenu fab;  //悬浮菜单按钮
    private FloatingActionButton fab_robot;  //呼叫机器人
    private FloatingActionButton fab_setting; //设置
    private FloatingActionButton fab_vending_machine; //售卖机
    public boolean  mHandlerFlag=true;
    private String timeNow;//当前时间
    private String dateNow; //当前日期
    private String orderNowNo;//当前订单编号
    private String deskNo = "4号桌";
    private boolean running = true;

    public void stop() {
        this.running = false;
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int close=0;
            tv_order_title.setText(R.string.main_title);
            bt_order_add_settlement.setText(R.string.settlement);
            bt_order_add_water.setText(R.string.add_water);
            bt_order_add_rice.setText(R.string.add_rice);
            tv_order_price.setText(R.string.order_menu_sum);
            tv_order_sum_name.setText(R.string.order_sum);
            bt_order_place.setText(R.string.order_place);

            Bundle data = new Bundle();
            data = msg.getData();

//            Log.d("data", "id:"+data.get("id"));
//            Log.d("data", "id:"+data.get("id").toString());
//            Toast.makeText(MainActivity.this, data.get("id").toString(), Toast.LENGTH_SHORT).show();
            switch (msg.what) {
                case send_msg_code1:
                    lv_main.setAdapter(new FoodTypeMenuAdapter(list, MainActivity.this));
                    lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            setTabSelection(position);
                        }
                    });
                    break;
                case send_msg_code2:
                    double money = 0.0;
                    mAdapter_order = new FoodOrderAdapter(newList, MainActivity.this);
                    lv_main_order.setAdapter(mAdapter_order);
                    for (OrderInfo orderInfo : newList) {
                        money += orderInfo.getCount() * orderInfo.getPrice();
                    }
                    tv_order_sum.setText("￥:" + money);
                    break;
                case send_msg_code3:
                    Bundle bundle = msg.getData();
                    int num = bundle.getInt("upOrder");
//                    if(num==1){
//                        bt_order_place.setEnabled(false);
//                    }else {
//                    bt_order_place.setEnabled(true);}
                    bt_order_place.setEnabled(num >= 1 ? false : true);
                    isFlag(true);
                    break;
                case send_msg_code4:
                    Bundle bundle1 = msg.getData();
                    int num1 = bundle1.getInt("Select");
                    newList  = (List<OrderInfo>) bundle1.getSerializable("List");

                    if(num1>1){
                        mAdapter_order = new FoodOrderAdapter(newList, MainActivity.this);
                        lv_main_order.setAdapter(mAdapter_order);
                        mAdapter_order.notifyDataSetChanged();
                        bt_order_place.setEnabled(true);

                        break;
                    }
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
        //注册事件
        EventBus.getDefault().register(this);
        initView();
        initData();

        new Thread(new MyThread()).start();
//        post();

    }

    private void initView() {
        lin_one = (LinearLayout) findViewById(R.id.lin_one);
        lin_three = (LinearLayout) findViewById(R.id.lin_three);
        lv_main = (ListView) findViewById(R.id.lv_main);
        lv_main_order = (ListView) findViewById(R.id.lv_main_order);
        tv_order_title = (TextView) findViewById(R.id.tv_order_title);
        bt_order_add_settlement = (Button) findViewById(R.id.bt_order_add_settlement);
        bt_order_add_water = (Button) findViewById(R.id.bt_order_add_water);
        bt_order_add_rice = (Button) findViewById(R.id.bt_order_add_rice);
        bt_order_place = (Button) findViewById(R.id.bt_order_place);
        tv_order_sum = (TextView) findViewById(R.id.tv_order_sum);
        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        tv_order_sum_name = (TextView) findViewById(R.id.tv_order_sum_name);
        myContent = (FrameLayout) findViewById(R.id.myContent);
        fab = (FloatingActionMenu) findViewById(R.id.fab);
        fab.setClosedOnTouchOutside(true);

        fab_vending_machine = (FloatingActionButton) findViewById(R.id.fab_vending_machine);
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_robot = (FloatingActionButton) findViewById(R.id.fab_robot);


        fragmentManager = getSupportFragmentManager();


        fragment1 = new Blank1Fragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.myContent, fragment1);
        transaction.commit();
//        mNoNetworkFab.setOnClickListener(this);
    }


    private void initData() {
        list = new ArrayList<MenuButton>();
        list_order = new ArrayList<>();
        selectFoodemu();
        bt_order_place.setOnClickListener(listerner);
        bt_order_add_water.setOnClickListener(listerner);

        fab_robot.setOnClickListener(this);
        fab_setting.setOnClickListener(this);
        fab_vending_machine.setOnClickListener(this);

    }

    /**
     * 查询菜单
     */
    private void selectFoodemu() {
        dbManager = new DbManger(this);
        dbManager.copyDBFile();
        list = dbManager.getAlldDscribe();
        mHandler.sendEmptyMessage(send_msg_code1);
    }


    View.OnClickListener listerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_order_place:
//
//               new Thread(runnable).start();  dateNow = VeDate.getStringDateShort();

                    if (newList == null) {
                        Toast.makeText(MainActivity.this, "你还没有点菜", Toast.LENGTH_SHORT).show();
                    } else {
                        mHandlerFlag=false;
                        new Thread(runnable).start();
                    }
                    break;
                case R.id.bt_order_add_water:

                    break;
                default:
                    break;
            }
        }
    };

    Runnable runnable = new Runnable() {
        private Connection con = null;
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //引用代码此处需要修改，address为数据IP，Port为端口号，DBName为数据名称，UserName为数据库登录账户，Password为数据库登录密码
                con = (Connection) DriverManager.getConnection("jdbc:mysql://103.45.11.232/lm_food",
                        "root", "root");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //测试数据库连接
            try {
                selectCONSUMPTIONID(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable runnable1 = new Runnable() {
        private Connection con = null;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //引用代码此处需要修改，address为数据IP，Port为端口号，DBName为数据名称，UserName为数据库登录账户，Password为数据库登录密码
                con = (Connection) DriverManager.getConnection("jdbc:mysql://103.45.11.232/lm_food",
                        "root", "root");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //测试数据库连接
            try {
                selectClear(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

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
                    Thread.sleep(2000);// 线程暂停10秒，单位毫秒
                    new Thread(runnable1).start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
    /**
     *   插入临时订单
     * @param con1
     * @return
     */
    public boolean insert(Connection con1) {
        Statement stmt;
        String sql;
        int count = 0;
        try {
            for (OrderInfo orderInfo : newList) {
                dateNow = VeDate.getStringDateShort();
                timeNow = VeDate.getTimeShort();
                orderNowNo = getOrderId();
                sql = "INSERT INTO " + DESK_TEMP + "(`date`, `time`, `desk_no`, `consumptionID`, `foodName`, `foodPrice`, `foodCount`)" + " VALUES ('" + dateNow + "', '" + timeNow+ "', '" + deskNo + "','" + orderNowNo + "', '" + orderInfo.getName() + "', '" + orderInfo.getPrice() + "', " + orderInfo.getCount() + ");";
                //创建Statement
                stmt = con1.createStatement();
                Log.d("MainActivity", sql);

                int rs = stmt.executeUpdate(sql);

                count += rs;
            }
            Message msg = new Message();
            msg.what = send_msg_code3;
            Bundle bundle = new Bundle();
            bundle.putInt("upOrder", count);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
            con1.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 查询最后一个订单
     * @param con1
     * @throws java.sql.SQLException
     */
    public void selectCONSUMPTIONID(Connection con1) throws java.sql.SQLException {
        String  isNull="";
        try {
            String sql = "select "+CONSUMPTIONID+" from "+DESK_TEMP+" order by "+DESKTEMP_TIME+" desc limit 0,1;";
            Statement stmt = con1.createStatement();        //创建Statement
            //ResultSet类似Cursor
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next()){//将结果集信息添加到返回向量中
                isNull=rs.getString("CONSUMPTIONID");
            }
            if(!isNull.equals("")){
                rewriteOrdera(isNull);
            }
            insert(con1);
            rs.close();
            stmt.close();
        } catch (SQLException e) {

        } finally {
            if (con1 != null)
                try {
                    con1.close();
                } catch (SQLException e) {
                }
        }
    }


    /**
     * 定时查询
     * @param con1
     * @return
     * @throws java.sql.SQLException
     */
    public boolean selectClear(Connection con1) throws java.sql.SQLException {
        int count=1;
        try {
            String sql = "select * from "+DESK_TEMP+" where "+DSK_NO+"='" +deskNo+"';";
            Log.d("MainActivity", sql);
            Statement stmt = con1.createStatement();        //创建Statement
            //ResultSet类似Cursor
            ResultSet rs = stmt.executeQuery(sql);
//            if(!rs.next()){
            newList.clear();
                while (rs.next()){
                    //                String data=rs.getString("date");
//                String time=rs.getString("time");
//                String desk_no=rs.getString("desk_no");
//                String consumptionID=rs.getString("consumptionID");
                String foodName=rs.getString("foodName");
                String foodPrice=rs.getString("foodPrice");
                int foodCount=rs.getInt("foodCount");

                OrderInfo info=new OrderInfo(count++,foodName,Double.valueOf(foodPrice),foodCount,false);
                    Log.d("MainActivity", "count:" + count);
                newList.add(info);
                }

//            }else{
//                num=2;
//            }
            Message message = new Message();
            message.what = send_msg_code4;
            Bundle bundle = new Bundle();
            bundle.putSerializable("List", (Serializable) newList);
            bundle.putInt("Select", count);
            message.setData(bundle);
            mHandler.sendMessage(message);
        } catch (SQLException e) {

        } finally {

            if (con1 != null)
                try {
                    con1.close();
                } catch (SQLException e) {
                }
        }

        return false;
    }




    @SuppressLint("NewApi")
    private void setTabSelection(int index) {

        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index + 1) {
            case 1:
                if (fragment1 == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    fragment1 = new Blank1Fragment();
                    transaction.add(R.id.myContent, fragment1);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(fragment1);
                }
                break;
            case 2:
                if (fragment2 == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    fragment2 = new Blank2Fragment();
                    transaction.add(R.id.myContent, fragment2);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(fragment2);
                }
                break;
            case 3:
                if (fragment3 == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    fragment3 = new Blank3Fragment();
                    transaction.add(R.id.myContent, fragment3);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(fragment3);
                }
                break;
            case 4:
                if (fragment4 == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    fragment4 = new Blank4Fragment();
                    transaction.add(R.id.myContent, fragment4);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(fragment4);
                }
                break;
            case 5:
                if (fragment5 == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    fragment5 = new Blank5Fragment();
                    transaction.add(R.id.myContent, fragment5);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(fragment5);
                }
                break;
            default:
                break;
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
        if (fragment4 != null) {
            transaction.hide(fragment4);
        }
        if (fragment5 != null) {
            transaction.hide(fragment5);
        }
    }

    private int index = 1;
    private int count = 1;


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMoonEvent(OrderInfo info ) {
        info = new OrderInfo(index, info.getName(), info.getPrice(), count,true);
        Log.d("info", "info.isFlag():" + info.isFlag());



     if(info.isFlag()){

         isFlag(false);

     }
   new Thread(new MyThread()).interrupt();

        list_order.add(info);
        newList = new ArrayList<OrderInfo>();
        Iterator<OrderInfo> it = list_order.iterator();
        while (it.hasNext()) {
            OrderInfo d = it.next();

            Iterator<OrderInfo> temp = newList.iterator();
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

                newList.add(d);
                d.setCount(1);
            }
        }


        mHandler.sendEmptyMessage(send_msg_code2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // land
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // port
            }
        } catch (Exception ex) {
        }
    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab_setting:

                new DialogTablde().showDialog(MainActivity.this);
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
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
        }
    }


   public void isFlag(boolean  falg){
       mHandlerFlag=falg;
       if(mHandlerFlag== true){
           new Thread(new MyThread()).start();

       }else {
           MyThread callable = new MyThread();
           Thread th = new Thread(callable);
           th.interrupt();
           callable.stop();



           callable.stop();
           mHandler.removeCallbacks(new MyThread());
           mHandler.removeCallbacks(new MyThread());
           new Thread(new MyThread()).interrupt();
       }
   }

    private void hideNavigationBar() {
        // TODO Auto-generated method stub
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

}
