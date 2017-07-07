package com.food.lmln.food.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.ViewUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.food.lmln.food.inteface.IActivity;

/**
 * Created by Weili on 2017/6/2.
 */

public class BaseActivity extends FragmentActivity implements IActivity {

    protected Intent startIntent;
    private Toast mToast;
    protected int PULL_FINISH = 0;
    public String ISREFRESH = "isReFresh";

    @SuppressLint("HandlerLeak")
    public Handler handlerMain = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Msg(msg);
        }
    };

    public void Msg(Message msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startIntent = new Intent();

//        LogUtils.customTagPrefix = "jwzhangjie";
        initView();
        initListener();
        initData();
    }


    protected void initView() {
    }

    protected void initListener() {

    }

    protected void initData() {

    }

    protected void showInfo(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    protected void showInfo(int text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    @Override
    public void startActivity(Class<?> cls, boolean isClose) {
        startIntent.setClass(this, cls);
        startActivity(startIntent);
        if (isClose) {
            this.finish();
        }
    }

    @Override
    public void startActivity(Class<?> cls) {
        startIntent.setClass(this, cls);
        startActivity(startIntent);
    }

    @Override
    public void startActivity(Class<?> cls, Bundle bundle, boolean isClose) {
        startIntent.setClass(this, cls);
        startIntent.putExtras(bundle);
        startActivity(startIntent);
        if (isClose) {
            this.finish();
        }
    }

    @Override
    public void startActivityForResult(int request) {
    }

    @Override
    public void startActivityForResult(int request, Class<?> cls,
                                       boolean isClose) {
        startIntent.setClass(this, cls);
        super.startActivityForResult(startIntent, request);
        if (isClose) {
            this.finish();
        }
    }

    @Override
    public void startActivityForResult(int request, Class<?> cls) {
        startIntent.setClass(this, cls);
        super.startActivityForResult(startIntent, request);
    }

    @Override
    public void startActivityForResult(int request, Class<?> cls, Bundle bundle) {
        startIntent.setClass(this, cls);
        startIntent.putExtras(bundle);
        super.startActivityForResult(startIntent, request);
    }

}
