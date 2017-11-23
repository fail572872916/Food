package com.food.lmln.food.receiver;

import android.app.Application;


import cn.jpush.android.api.JPushInterface;

/**
 *  @author Weli
 *  @time 2017-11-23  18:34
 *  @describe
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