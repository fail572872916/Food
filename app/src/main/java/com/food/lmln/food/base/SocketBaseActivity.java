package com.food.lmln.food.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.food.lmln.IBackService;
import com.food.lmln.food.R;
import com.food.lmln.food.bean.DeskInfo;
import com.food.lmln.food.db.Constants;
import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.db.SqlHelper;
import com.food.lmln.food.services.SocketService;

import java.util.List;

/**
 * @author Administrator
 * @name CarControl
 * @class name：com.example.administrator.carcontrol
 * @class describe
 * @time 2017-10-12 16:14
 * @change
 * @chang time
 * @class describe
 */
public class SocketBaseActivity extends AppCompatActivity {
    /***
     * 子类中完成抽象函数赋值
     实体中通过实现该全局接收器方法来处理接收到消息
     */
    public BaseMessageBackReceiver mReciver;
    private IntentFilter mIntentFilter;
    private Intent mServiceIntent;
    private LocalBroadcastManager localBroadcastManager;
    /**
     * IBackService ;
     * //通过调用该接口中的方法来实现数据发送
     */
    public IBackService iBackService;
    /**
     * 标记是否已经进行了服务绑定与全局消息注册
     */
    private boolean isBound = false;
    private boolean flag = false;
    public String deskNo = ""; //当前桌台号
    public String deskIp = ""; //连接ip
    SqlHelper helper;
    private SQLiteDatabase db;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iBackService = IBackService.Stub.asInterface(iBinder);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iBackService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = DbManger.getInstance(this);
        db = helper.getWritableDatabase();
        selectIpDesk();

    }

    public void startSocket() {

        if (mReciver != null && !flag) {
            if (!TextUtils.isEmpty(deskIp)) {
                SocketService.HOST =deskIp;
                initSocket();
                localBroadcastManager.registerReceiver(mReciver, mIntentFilter);
                isBound = bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
                Log.d("SocketBaseActivity", "isBound:" + isBound);
                startService(mServiceIntent);
                flag = true;
            }

        }
    }

    /**
     * 发送socket
     *
     * @param data 发送参数
     * @return ture/false;
     */
    public boolean socketSend(final String data) {
        try {
            if (iBackService == null) {
                Toast.makeText(this, R.string.server_off, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                boolean isSend = iBackService.sendMessage(data);
                if (!isSend) {
                    Toast.makeText(this, R.string.link_fail, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }
    //在onResume()方法注册
    @Override
    protected void onResume() {

        super.onResume();
    }

    /**
     * 发送
     *
     * @param str
     * @return
     */
    public boolean send(String str) {
        Log.d("SocketBaseActivity", str);
        try {
            iBackService.sendMessage(str);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 改变接连对象
     *
     * @param string
     */
    public void change(String string) {
        if (flag) {
            Log.d("SocketBaseActivity", string);
            if (mReciver != null) {
                localBroadcastManager.unregisterReceiver(mReciver);
                Log.d("SocketBaseActivity", "我走了");
            }
            stopService();
            SocketService.HOST = string;
            if (localBroadcastManager == null) {
                initSocket();
            }
            localBroadcastManager.registerReceiver(mReciver, mIntentFilter);
            isBound = bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
            startService(mServiceIntent);
        } else {
            Log.d("SocketBaseActivity", "aa");
            flag = true;
            SocketService.HOST = string;
            if (localBroadcastManager == null) {
                initSocket();
            }
            localBroadcastManager.registerReceiver(mReciver, mIntentFilter);
            isBound = bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
            startService(mServiceIntent);
        }

    }

    public void stopService() {

        if ( flag) {
            if (isBound) {
                unbindService(conn);
                isBound=false;
            }
            localBroadcastManager.unregisterReceiver(mReciver);
            stopService(mServiceIntent);

        }
    }

    @Override
    public void onDestroy() {
        stopService();

        super.onDestroy();
    }

    public void initSocket() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
//        mReciver = new MessageBackReciver();
        mServiceIntent = new Intent(this, SocketService.class);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SocketService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(SocketService.MESSAGE_ACTION);
    }

    /**
     * @author Weli
     * @time 2017-10-16  10:50
     * @describe 广播接收器
     */
    public abstract class BaseMessageBackReceiver extends BroadcastReceiver {
        /**
         * 　　register　　Receiver
         *
         * @param context
         * @param intent
         */
        @Override
        public abstract void onReceive(Context context, Intent intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
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
     * 查询桌台与Ip
     */
    private void selectIpDesk() {
        db = helper.getWritableDatabase();
        int set = DbManger.getCountPerson(db, Constants.DESK_INFO);
        if (set < 1) {

        } else {
            db = helper.getWritableDatabase();
            List<DeskInfo> li = DbManger.selectDeskInfo(db, Constants.DESK_INFO);
            if (li.size() > 0) {
                deskNo = li.get(0).getLocal_desk();
                deskIp = li.get(0).getLocal_ip();
            }
        }
    }
}