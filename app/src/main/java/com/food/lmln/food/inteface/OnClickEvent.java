package com.food.lmln.food.inteface;

import android.view.View;

/**
 * Created by Weili on 2017/6/28.

 * Time: 20:22
 * 类描述：
 *
 * @version :
 */
public abstract class OnClickEvent implements View.OnClickListener {

    private static long lastTime;

    public abstract void singleClick(View v);
    private long delay;

    public OnClickEvent(long delay) {
        this.delay = delay;
    }

    @Override
    public void onClick(View v) {
        if (onMoreClick(v)) {
            return;
        }
        singleClick(v);
    }

    public boolean onMoreClick(View v) {
        boolean flag = false;
        long time = System.currentTimeMillis() - lastTime;
        if (time < delay) {
            flag = true;
        }
        lastTime = System.currentTimeMillis();
        return flag;
    }
}
