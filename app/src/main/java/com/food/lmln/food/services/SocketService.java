package com.food.lmln.food.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.food.lmln.IBackService;
import com.food.lmln.food.R;
import com.food.lmln.food.db.Constants;
import com.food.lmln.food.utils.JsonUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Socket service
 *  @author Weli
 *  @time 2017-11-17  9:55
 *  @describe
 */
public class SocketService extends Service {
    private static final String TAG = "SocketService";
    public MyCountDownTimer mc;
    /**
     * HEART_BEAT_RATE    心跳包频率
     */
    private static final int TIME_OUT = 5 * 1000;
    public static String HOST = "192.168.0.109";
    public static final int PORT = 30000;
    int ss = 1000;

    int dayMill = 10000;
    private ReadThread mReadThread;
    public static final String MESSAGE_ACTION = "com.food.lmln.food.socket";
    public static final String HEART_BEAT_ACTION = "com.food.lmln.food.heart";
    public static final String HEART_BEAT_STRING_RECEIVE = "{\"flaglist\":null,\"flag\":null,\"socket_ip\":null,\"message\":null,\"operationCode\":null,\"heartBeat\":\"0x0F\"}";
    public static final String HEART_BEAT_STRING = "{\"flaglist\":null,\"flag\":null,\"socket_ip\":null,\"message\":null,\"operationCode\":null,\"heartBeat\":\"0x0h\"}";
    public static final String HEART_BEAT_BREAK = "";
    private long sendTime = 0L;
    /**
     * 弱引用 在引用对象的同时允许对垃圾对象进行回收
     */
    private LocalBroadcastManager mLocalBroadcastManager;
    private WeakReference<Socket> mSocket;
    private IBackService.Stub iBackService = new IBackService.Stub() {
        @Override
        public boolean sendMessage(String message) throws RemoteException {
            return sendMsg(message);
        }
    };

    public boolean linkSocket = false;
    private InitTask initTask;
    private int millis=15*1000;

    @Override
    public IBinder onBind(Intent arg0) {
        return iBackService;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        initTask = new InitTask();
        initTask.execute();
        Log.d(TAG, "create");
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mc = new MyCountDownTimer(dayMill, ss).getInstance();
    }
    public boolean sendMsg(String msg) {
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        try {
            if (soc != null && soc.isConnected() && !soc.isClosed()) {
                OutputStream os = soc.getOutputStream();
                String message = msg + "\r\n";
                os.write(message.getBytes());
                os.flush();
                //每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
                sendTime = System.currentTimeMillis();
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 初始化socket
     *
     * @throws UnknownHostException
     * @throws IOException
     */
    private void initSocket() throws IOException {
        if (mc != null) {
            mc.cancel();
            mc.start();
        }

        Socket socket;
        mSocket = null;
        socket = initSocketLInk();

        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            mSocket = new WeakReference<>(socket);
            mReadThread = new ReadThread(socket);
            mReadThread.start();
            sendMsg(JsonUtils.initSend(getString(R.string.text_tag_equipment), HOST, String.valueOf(new JSONObject()), Constants.ONLINE, ""));
            linkSocket = true;
        }
    }

    private static Socket initSocketLInk() {

        Socket socket;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST, PORT), TIME_OUT);
        } catch (UnknownHostException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return socket;
    }

    /**
     * 释放socket
     *
     * @param mSocket
     */
    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (sk != null && !sk.isClosed()) {
                    sk.close();

                    Log.d(TAG, "close");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 服务被销毁时调用
     */
    @Override
    public void onDestroy() {
        Log.e("Service", "onDestroy");
        mc.cancel();
        mHandler.removeCallbacks(mReadThread);
        if (mReadThread != null) {
            mReadThread.release();
        }
        releaseLastSocket(mSocket);
        if (initTask != null) {
            initTask.cancel(true);
        }
        super.onDestroy();
    }

    private class InitTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute() called");
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Void doInBackground(String... params) {
            try {
                initSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute(Result result) called");

        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
            Log.i(TAG, "onCancelled() called");
        }
    }

    private class ReadThread extends Thread {
        private WeakReference<Socket> mWeakSocket;
        private boolean isStart = true;

        public ReadThread(Socket socket) {
            mWeakSocket = new WeakReference<>(socket);
        }

        private void release() {
            isStart = false;
            if (mWeakSocket != null) {
                releaseLastSocket(mWeakSocket);
            }
        }

        @SuppressLint("NewApi")
        @Override
        public void run() {
            super.run();
            Socket socket = mWeakSocket.get();
            if (null != socket) {
                try {
                    InputStream is = socket.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int length;
                    while (!socket.isClosed() && !socket.isInputShutdown()
                            && isStart && ((length = is.read(buffer)) != -1)) {
                        if (length > 0) {
                            if (mc != null) {
                                mc.cancel();
                                mc.start();
                                linkSocket = true;
                            }
                            String message = new String(Arrays.copyOf(buffer,
                                    length)).trim();
                            Log.i(TAG, "收到服务器发送来的消息：" + message + "______________________");
                            // 收到服务器过来的消息，就通过Broadcast发送出去
                            //处理心跳回复

                            if (TextUtils.isEmpty(message)) {
                                continue;
                            } else {
                                String code = JsonUtils.jsonResolveType(message);
                                if (code.equals(Constants.SOCKET_MSG_CAR_HEART)) {
                                    linkSocket = true;
                                    if (!sendMsg(HEART_BEAT_STRING_RECEIVE)) {
                                        linkSocket = false;
                                    }
                                    Intent intent = new Intent(HEART_BEAT_ACTION);
                                    mLocalBroadcastManager.sendBroadcast(intent);

                                } else if (code.equals(Constants.ONLINE)) {
                                    linkSocket = true;
                                    continue;

                                } else {
                                    //其他消息回复
                                    Intent intent = new Intent(MESSAGE_ACTION);
                                    intent.putExtra("message", message);
                                    intent.putExtra("code", code);
                                    mLocalBroadcastManager.sendBroadcast(intent);
                                    linkSocket = true;
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    release();
                    releaseLastSocket(mSocket);
                    Log.d(TAG, "服务器断开");
                    linkSocket = false;
                    e.printStackTrace();
                }
            }
        }
    }
    class MyCountDownTimer extends CountDownTimer {
        private MyCountDownTimer myCountDownTimer;
        /**
         * When the countdown is 10, take effect
         * MySelf break timer 7s
         * MySelf  check link timer 3s
         */
        private int anInt = 7;
        private int anInt1 = 3;
        private synchronized MyCountDownTimer getInstance() {
            if (myCountDownTimer == null) {
                myCountDownTimer = new MyCountDownTimer(dayMill, ss);
            }
            return myCountDownTimer;
        }

        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mc.cancel();
        }

        /**
         * @param millisUntilFinished this is millisUntilFinished ,You have to use seconds  /1000
         */
        @Override
        public void onTick(long millisUntilFinished) {
            int num = (int) (millisUntilFinished / 1000);
            if (num == anInt) {
                linkSocket = false;

            } else if (millisUntilFinished == 0) {
                mc.cancel();
                mc.start();
            } else if (num < anInt1 && !linkSocket) {
                try {
                    Thread.sleep(millis);
                    if (initTask != null) {
                        releaseLastSocket(mSocket);
                        initTask.cancel(true);
                        initTask = new InitTask();
                        initTask.execute();
                        mHandler.removeCallbacks(mReadThread);
                        releaseLastSocket(mSocket);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}