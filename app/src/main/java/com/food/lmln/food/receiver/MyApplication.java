package com.food.lmln.food.receiver;

import android.app.Application;


import cn.jpush.android.api.JPushInterface;

/**
 * Created by HDL on 2016/9/8.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化sdk
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
    }
}