package com.food.lmln.food.services;

/**
 * Created by Administrator on 2017/8/25.
 * socket服务类
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.food.lmln.IBackService;

public class BackService extends Service {

    private static final String TAG = "BackService";
    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 2000;
    private static final int TIME_OUT = 5 * 1000;
    /**
     * 主机IP地址
     */
    public static  String HOST = "192.168.16.136";
    /**
     * 端口号
     */
    public static  int PORT = 30000;
    /**
     * 消息广播
     */
    public static final String MESSAGE_ACTION = "com.food.message_ACTION";
    /**
     * 心跳广播
     */
    public static final String HEART_BEAT_ACTION = "com.food.heart_beat_ACTION";
    private long sendTime = 0L;
    /**
     * 弱引用 在引用对象的同时允许对垃圾对象进行回收
     */
    private WeakReference<Socket> mSocket;
    private ReadThread mReadThread;
    private InitSocketThread initSockeTh = new InitSocketThread();
    private MyRunnable myRunnable = new MyRunnable();
    private IBackService.Stub iBackService = new IBackService.Stub() {
        @Override
        public boolean sendMessage(String message) throws RemoteException {
            return sendMsg(message);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return (IBinder) iBackService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSockeTh.start();
    }

    // 发送心跳包
    private Handler mHandler = new Handler();
    class MyRunnable implements Runnable {
        private MyRunnable() {
        }
        private MyRunnable instance;

        public MyRunnable getInstance() {
            if (instance == null) {
                instance = new MyRunnable();
            }
            return instance;
        }
        @Override
        public void run() {
            boolean isSuccess = sendMsg("0xFF");// 就发送一个\r\n过去, 如果发送失败，就重新初始化一个socket
            if (!isSuccess) {
                mHandler.removeCallbacks(myRunnable);
                if (mReadThread != null) {
                    Log.d("fdsa", "333");
                    mReadThread.release();
                }
                releaseLastSocket(mSocket);
                initSockeTh = new InitSocketThread();
                initSockeTh.start();
                Log.d("fdsa", "444");
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    }


    public boolean sendMsg(String msg) {

        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        Log.d("fdsa", "555");
        try {
            if (soc != null && soc.isConnected() && !soc.isClosed()) {
                OutputStream os = soc.getOutputStream();
                String message = msg + "\r\n";
                os.write(message.getBytes());
                os.flush();
                sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    // 初始化socket
    private void initSocket() throws UnknownHostException, IOException {
        Socket socket ;
        mSocket = null;
        socket = initSocketddd();
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            mSocket = new WeakReference<>(socket);
            mReadThread = new ReadThread(socket);
            mReadThread.start();
            mHandler.removeCallbacks(initSockeTh);
            mHandler.removeCallbacks(myRunnable);
            mHandler.postDelayed(myRunnable, HEART_BEAT_RATE);// 初始化成功后，就准备发送心跳包
        } else {
            mHandler.removeCallbacks(initSockeTh);
            mHandler.removeCallbacks(myRunnable);
            if (mReadThread != null) {
                mHandler.removeCallbacks(mReadThread);
                mReadThread.release();

            }
            releaseLastSocket(mSocket);
            mHandler.postDelayed(myRunnable, HEART_BEAT_RATE);// 初始化成功后，就准备发送心跳包
        }
    }

    @Nullable
    private static Socket initSocketddd() {

        Socket socket ;
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


    // 释放socket
    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (sk != null && !sk.isClosed()) {
                    sk.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class InitSocketThread extends Thread {
        private InitSocketThread initSockeTh;

        private InitSocketThread() {
        }

        public synchronized InitSocketThread getInstance() {
            if (initSockeTh == null) {
                initSockeTh = new InitSocketThread();
            }
            return initSockeTh;
        }

        @Override
        public void run() {
            super.run();

            try {
                initSocket();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
            releaseLastSocket(mWeakSocket);
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
                    int length ;
                    while (!socket.isClosed() && !socket.isInputShutdown()
                            && isStart && ((length = is.read(buffer)) != -1)) {
                        if (length > 0) {
                            String message = new String(Arrays.copyOf(buffer,
                                    length)).trim();
                            Log.i(TAG, "收到服务器发送来的消息：" + message);
                            // 收到服务器过来的消息，就通过Broadcast发送出去
                            if ("ok".equals(message)) {// 处理心跳回复
                                Intent intent = new Intent(HEART_BEAT_ACTION);
                                sendBroadcast(intent);
                            } else {
                                // 其他消息回复
                                Intent intent = new Intent(MESSAGE_ACTION);
                                intent.putExtra("message", message);
                                sendBroadcast(intent);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 服务被销毁时调用
     */
    @Override
    public void onDestroy() {
        Log.e("Service", "onDestroy");
        releaseLastSocket(mSocket);
        mHandler.removeCallbacks(mReadThread);
        mHandler.removeCallbacks(initSockeTh);
        mHandler.removeCallbacks(myRunnable);
        Log.e("Service", "执行了");
        super.onDestroy();
    }

}
