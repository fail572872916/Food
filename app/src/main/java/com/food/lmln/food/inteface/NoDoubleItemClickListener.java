package com.food.lmln.food.inteface;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Weili on 2017/6/28.
 * 防止重复点击
 */

public abstract class NoDoubleItemClickListener implements AdapterView.OnItemClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 800;
    private long lastClickTime = 0;
   Context montext;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            NoDoubleItemClickListener(view,position);
        }
    }

    public abstract void NoDoubleItemClickListener(View v,int position);
}