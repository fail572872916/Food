package com.food.lmln.food.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.food.lmln.food.bean.DeskInfo;
import com.food.lmln.food.bean.MenuButton;
import com.food.lmln.food.bean.OrderInfo;
import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.db.MysqlDb;
import com.food.lmln.food.fragment.BlankFragment;
import com.food.lmln.food.fragment.Blank2Fragment;
import com.food.lmln.food.fragment.Blank3Fragment;
import com.food.lmln.food.utils.VeDate;
import com.food.lmln.food.utils.socket_client;
import com.food.lmln.food.view.DialogTablde;
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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.food.lmln.food.db.Constant.DESK_TEMP;
import static com.food.lmln.food.db.Constant.DSK_NO;
import static com.food.lmln.food.db.Constant.ORDERID;
import static com.food.lmln.food.db.Constant.ORDERINFO;
import static com.food.lmln.food.db.Constant.ORDERTABLE;
import static com.food.lmln.food.db.Constant.PASSWORD;
import static com.food.lmln.food.db.Constant.PRINTIN;
import static com.food.lmln.food.db.Constant.SQLURL;
import static com.food.lmln.food.db.Constant.USERNAME;
import static com.food.lmln.food.db.Constant.send_msg_code1;
import static com.food.lmln.food.db.Constant.send_msg_code2;
import static com.food.lmln.food.db.Constant.send_msg_code3;
import static com.food.lmln.food.db.Constant.send_msg_code4;
import static com.food.lmln.food.utils.FileUtils.rewriteOrdera;
import static com.food.lmln.food.utils.OrderUtils.getOrderId;
public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
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
    static socket_client client=new socket_client();
    FrameLayout myContent;
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private BlankFragment fragment1;
    private Blank2Fragment fragment2;
    private Blank3Fragment fragment3;
    private DbManger dbManager;
    private List<OrderInfo> newList =new ArrayList<>();
    private List<OrderInfo> addList =new ArrayList<>();
    private List<OrderInfo> list_order =new ArrayList<>();
    private List<MenuButton> listRight =new ArrayList<MenuButton>();
//    private List<MenuButton> getRight =new ArrayList<MenuButton>();
    private FloatingActionMenu fab;  //悬浮菜单按钮
    private FloatingActionButton fab_robot;  //呼叫机器人
    private FloatingActionButton fab_setting; //设置
    private FloatingActionButton fab_vending_machine; //售卖机
    public boolean  mHandlerFlag=true;
    private String timeNow;//当前时间
    private String dateNow; //当前日期
    private String orderNowNo;//当前订单编号
    private String deskNo = "4号桌";
    private   int stopCode=2;
    String serverIP_str ="192.168.0.198";
    String desk_num_str =deskNo;
    private Connection conn; //Connection连接
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
                           Log.d("aaa", listRight.get(0).getName());
                           fragment1 = new BlankFragment();

                        String  name=listRight.get(0).getName();
//                           EventBus.getDefault().post(new DeskInfo(name,name));
                           lv_main.setAdapter(new FoodTypeMenuAdapter(listRight, MainActivity.this));
                    lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            setTabSelection(position ,String.valueOf(listRight.get(position).getName()));
                        }
                    });
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
                    newList  = (List<OrderInfo>) bundle1.getSerializable("List");
                    if(stopCode==2){
                        if(num1>1 ||  newList.size()>0){
                            mAdapter_order = new FoodOrderAdapter(newList, MainActivity.this);
                            lv_main_order.setAdapter(mAdapter_order);
                            mAdapter_order.notifyDataSetChanged();
                            bt_order_place.setEnabled(true);
                            break;
                        }
                    }else{
                        newList.clear();
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
        initSokect();

    }

    /**
     * 初始化
     */
    private void initSokect() {
        if(serverIP_str.isEmpty()&&desk_num_str.isEmpty()){
            System.out.println("主机信息为空，请补充后再试试");
        }else{
            // 连线 server
            client.runclient(serverIP_str);
            //此线程为接收菜单的线程，循环接收，
            new Thread(){
                @Override
                public void run() {
                    while (true) {
                        if(client.i){

                            client.i = false;
                        }else if(client.j){

                            client.j=false;
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
        bt_order_place.setOnClickListener(listerner);
        bt_order_add_water.setOnClickListener(listerner);
     
        fab_vending_machine = (FloatingActionButton) findViewById(R.id.fab_vending_machine);
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_robot = (FloatingActionButton) findViewById(R.id.fab_robot);
        fab_robot.setOnClickListener(this);
        fab_setting.setOnClickListener(this);
        fab_vending_machine.setOnClickListener(this);
        myContent = (FrameLayout) findViewById(R.id.myContent);
        fab = (FloatingActionMenu) findViewById(R.id.fab);
        fab.setClosedOnTouchOutside(true);

        fragment1 = new BlankFragment();
        FragmentManager   fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myContent, fragment1);
        transaction.commit();
        dbManager = new DbManger(this);
        dbManager.copyDBFile();
        int a=dbManager.selectDeskCount();


    }
    /**
     * 查询菜单
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                listRight = MysqlDb.selectCuisine(conn, "select  * from  "+ORDERTABLE+"");
                mHandler.sendEmptyMessage(send_msg_code1);
            }
        }).start();


    }
    View.OnClickListener listerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_order_place:
                    if (addList == null) {
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
                con = (Connection) DriverManager.getConnection("jdbc:mysql://120.77.221.1:3036/lm_food",
                        "root", "lm123456");
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
                    conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                    newList = MysqlDb.selectRiht(conn, "select  * from   desk_temp where desk_no='4号桌'");
                    Log.d("MyThread", "newList:" + newList);
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
     *   插入临时订单
     * @param con1
     * @return
     */
    public boolean insert(Connection con1) {
        Statement stmt;
        String sql;
        int count = 0;
        try {
            orderNowNo = getOrderId();
            for (OrderInfo orderInfo : addList) {
                dateNow = VeDate.getStringDateShort();
                timeNow = VeDate.getTimeShort();
                sql = "INSERT INTO " + DESK_TEMP + "(`date`, `time`, `desk_no`, `consumptionID`, `foodName`, `foodPrice`, `foodCount`)" + " VALUES ('" + dateNow + "', '" + timeNow+ "', '" + deskNo + "','" + orderNowNo + "', '" + orderInfo.getName() + "', '" + orderInfo.getPrice() + "', " + orderInfo.getCount() + ");";
                //创建Statement
                stmt = con1.createStatement();
                int rs = stmt.executeUpdate(sql);
                count += rs;
            }
            String      sql1 = "INSERT INTO " + ORDERINFO + "( `order_id`, `desk`, `strat_time`, `end_time`, `order_date`, `order_describe`, `order_price`, `order_status`, `pay_type`)" + " VALUES ('"
                    + orderNowNo + "', '" + deskNo+ "', '" + timeNow + "','" + "" + "', '" + dateNow+ "', '" + ""+ "', '" + "20"+ "','" + 1+ "','" + 0+ "');";
            //创建Statement
            stmt = con1.createStatement();
            int rs1= stmt.executeUpdate(sql1);
         if(rs1>0){
            sendPrint();
         }else {

         }

            con1.close();
            stopCode=2;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 打印数据
     */
    private void sendPrint() {
                    JSONObject jsonObj = new JSONObject();//创建json格式的数据
            JSONArray jsonArr = new JSONArray();//json格式的数组
            try {
                for (OrderInfo orderInfo : list_order) {
                    JSONObject jsonObjArr = new JSONObject();
                    jsonObjArr.put("name", orderInfo.getName());
                    jsonObjArr.put("price", String.valueOf(orderInfo.getPrice()));
                    jsonObjArr.put("count", String.valueOf(orderInfo.getCount()));
                    jsonArr.put(jsonObjArr);//将json格式的数据放到json格式的数组里
                }
                jsonObj.put("orderInstruct", PRINTIN);//再将这个json格式的的数组放到最终的json对象中。
                jsonObj.put("desk_num_str", desk_num_str);//再将这个json格式的的数组放到最终的json对象中。
                jsonObj.put("print", jsonArr);//再将这个json格式的的数组放到最终的json对象中。
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MainActivity.client.sendfood(jsonObj);
            addList=null;
            list_order.clear();
            Message msg = new Message();
            msg.what = send_msg_code3;
            Bundle bundle = new Bundle();
            bundle.putInt("upOrder", count);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
    }

    /**
     * 查询最后一个订单
     * @param con1
     * @throws java.sql.SQLException
     */
    public void selectCONSUMPTIONID(Connection con1) throws java.sql.SQLException {
        String  isNull="";
        try {
            String sql = "select "+ORDERID+" from "+ORDERINFO+" order by "+ORDERID+" desc limit 0,1;";

            Statement stmt = con1.createStatement();        //创建Statement
            //ResultSet类似Cursor
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next()){//将结果集信息添加到返回向量中
                isNull=rs.getString(ORDERID);
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
    @SuppressLint("NewApi")
    private void setTabSelection(int index ,String tableName) {
        Log.d("MainActivity", tableName);
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        int num=++index;

        if(num%3==0){
            if (fragment3 == null) {
                // 如果NewsFragment为空，则创建一个并添加到界面上
                fragment3 = new Blank3Fragment();
                Bundle bundle2=new Bundle();
                bundle2.putString("foodName",tableName);
                fragment3.setArguments(bundle2);
                transaction.add(R.id.myContent, fragment3);
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
                fragment3 = new Blank3Fragment();
                Bundle bundle2=new Bundle();
                bundle2.putString("foodName",tableName);
                fragment3.setArguments(bundle2);
                transaction.add(R.id.myContent, fragment3);
            }
        }else if(num%3==2){
            if (fragment2 == null) {
                // 如果NewsFragment为空，则创建一个并添加到界面上
                fragment2 = new Blank2Fragment();
                Bundle bundle2=new Bundle();
                bundle2.putString("foodName",tableName);
                fragment2.setArguments(bundle2);
                transaction.add(R.id.myContent, fragment2);
            } else {
                // 如果NewsFragment不为空，则直接将它显示出来
                transaction.show(fragment2);
            }
        }
        else {
            if (fragment1 == null) {
                // 如果SettingFragment为空，则创建一个并添加到界面上
                fragment1 = new BlankFragment();
                Bundle bundle2=new Bundle();
                bundle2.putString("foodName",tableName);
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
                Bundle bundle2=new Bundle();
                bundle2.putString("foodName",tableName);
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
     *接收传过来的值
     * @param info
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMoonEvent(OrderInfo info ) {

        info = new OrderInfo(index, info.getName(), info.getPrice(), count,true);
        if(info.isFlag()){
            stopCode=1;
            isFlag(false);
        }
        new Thread(new MyThread()).interrupt();

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

                addList.add(d);
                d.setCount(1);
            }
        }

        mHandler.sendEmptyMessage(send_msg_code2);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        client.finish();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // land
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
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

    /**
     * 判断停止线程
     * @param falg
     */
    public void isFlag(boolean  falg){
        mHandlerFlag=falg;
        if(mHandlerFlag== true){
            new Thread(new MyThread()).start();
        }else {
            MyThread callable = new MyThread();
            Thread th = new Thread(callable);
            th.interrupt();
            callable.stop();
//            mHandler.removeCallbacks(runnable1);
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
    //定义一个回调接
}

