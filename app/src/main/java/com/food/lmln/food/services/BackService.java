package com.food.lmln.food.services;

/**
 * Created by Administrator on 2017/8/25.
 */


import java.io.IOException;
import java.io.InputStream;
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
    private static final String HOST = "192.168.0.108";
    /**
     * 端口号
     */
    public static final int PORT = 30000;
    /**
     * 消息广播
     */
    public static final String MESSAGE_ACTION = "org.feng.message_ACTION";
    /**
     * 心跳广播
     */
    public static final String HEART_BEAT_ACTION = "org.feng.heart_beat_ACTION";
    private long sendTime = 0L;
    /**
     * 弱引用 在引用对象的同时允许对垃圾对象进行回收
     */
    private WeakReference<Socket> mSocket;
    private ReadThread mReadThread;
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
        new InitSocketThread().start();
    }
    // 发送心跳包
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
                boolean isSuccess = sendMsg("0xFF");// 就发送一个\r\n过去, 如果发送失败，就重新初始化一个socket
            Log.d(TAG, "isSuccess:" + isSuccess);
                if (!isSuccess) {
                    mHandler.removeCallbacks(heartBeatRunnable);
                    mReadThread.release();
                    releaseLastSocket(mSocket);
                    new InitSocketThread().start();
                }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };


    public boolean sendMsg(String msg) {
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
            try {
            soc.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信


        } catch (Exception se) {
          throw new RuntimeException();
        }
        return true;
//        try {
//            if (soc != null && soc.isConnected() && !soc.isClosed()) {
////            if (!soc.isClosed() && !soc.isOutputShutdown()) {
//                OutputStream os = soc.getOutputStream();
//                String message = msg + "0xff";
//                os.write(message.getBytes());
//                os.flush();
//                sendTime = System.currentTimeMillis();// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
//                Log.i(TAG, "发送成功的时间：" + sendTime);
//            } else {
//                return false;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
    }

    // 初始化socket
    private void initSocket() throws UnknownHostException, IOException {
        Socket socket = null;
            socket = initSocketddd();
            if (socket != null && socket.isConnected() && !socket.isClosed()) {
                mSocket = new WeakReference<Socket>(socket);
                mReadThread = new ReadThread(socket);
                mReadThread.start();
                mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);// 初始化成功后，就准备发送心跳包
            } else {

            socket=null;
            }
    }
    private static Socket initSocketddd() {
        Socket socket = null;
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
                if (sk!=null && !sk.isClosed()) {
                    sk.close();
                }
                sk = null;
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class InitSocketThread extends Thread {
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

    public class ReadThread extends Thread {
        private WeakReference<Socket> mWeakSocket;
        private boolean isStart = true;

        public ReadThread(Socket socket) {
            mWeakSocket = new WeakReference<Socket>(socket);
        }

        public void release() {
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
                    int length = 0;
                    while (!socket.isClosed() && !socket.isInputShutdown()
                            && isStart && ((length = is.read(buffer)) != -1)) {
                        if (length > 0) {
                            String message = new String(Arrays.copyOf(buffer,
                                    length)).trim();
                            Log.i(TAG, "收到服务器发送来的消息：" + message);
                            // 收到服务器过来的消息，就通过Broadcast发送出去
                            if (message.equals("ok")) {// 处理心跳回复
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

}
