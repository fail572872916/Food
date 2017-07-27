package com.food.lmln.food.view;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.food.lmln.food.R;
import com.food.lmln.food.fragment.FragmentDialogPay;

/**
 * Created by Weili on 2017/7/27.
 */

public class MyPopWindow extends  PopupWindow implements View.OnClickListener {

    Context mContext;
    private  LayoutInflater mInflater;
    private  View mContentView;
    private ImageView im_pay_ali;
    private ImageView im_pay_weixin;


    public MyPopWindow(Context context) {
        super(context);

        this.mContext=context;
        //打气筒
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        //打气

        mContentView = mInflater.inflate(R.layout.pop_pay_type,null);

        //设置View
        setContentView(mContentView);


        //设置宽与高
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


        /**
         * 设置进出动画
         */
        setAnimationStyle(R.style.MyPopupWindow);

        /**
         * 设置背景只有设置了这个才可以点击外边和BACK消失
         */
        setBackgroundDrawable(new ColorDrawable());


        /**
         * 设置可以获取集点
         */
        setFocusable(true);

        /**
         * 设置点击外边可以消失
         */
        setOutsideTouchable(true);

        /**
         *设置可以触摸
         */
        setTouchable(true);


        /**
         * 设置点击外部可以消失
         */
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /**
                 * 判断是不是点击了外部
                 */
                if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                    return true;
                }
                //不是点击外部
                return false;
            }
        });


        /**
         * 初始化View与监听器
         */
        initView();

        initListener();
    }


    private void initView() {

        im_pay_ali = (ImageView) mContentView.findViewById(R.id.im_pay_ali);
        im_pay_weixin = (ImageView) mContentView.findViewById(R.id.im_pay_weixin);
    }

    private void initListener() {
        im_pay_ali.setOnClickListener(this);
        im_pay_weixin.setOnClickListener(this);
    }

    private OnItemClickListener mListener;



    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }

}