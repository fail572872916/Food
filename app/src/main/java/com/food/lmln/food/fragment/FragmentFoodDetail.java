package com.food.lmln.food.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.food.lmln.food.R;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.bean.OrderInfo;
import com.food.lmln.food.db.Constant;
import com.food.lmln.food.inteface.OnClickEvent;
import com.food.lmln.food.utils.HttpUtils;
import com.food.lmln.food.utils.MyBitmapUtil;
import com.food.lmln.food.utils.SystemUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/23.
 */

public class FragmentFoodDetail extends DialogFragment {
    private View view;
    private ImageView im_detail_food;
    private TextView tv_detail_food;
    private Button bt_detail_food;
    private String mParam1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(Constant.FOOD_DETAIL);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);  //点击外部不消失
        view = inflater.inflate(R.layout.fragment_food_detail, container, false);
        initView();
        return view;
    }
    private void initView() {
        im_detail_food = (ImageView) view.findViewById(R.id.im_detail_food);
        tv_detail_food = (TextView) view.findViewById(R.id.tv_detail_food);
        bt_detail_food = (Button) view.findViewById(R.id.bt_detail_food);
        intData();
    }

    private void intData() {
        if (mParam1 != null) {
            Gson gson = new Gson();
            final FoodinfoSmall foodinfoSmall = gson.fromJson(mParam1, FoodinfoSmall.class);
            tv_detail_food.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_detail_food.setTextSize(20);
            tv_detail_food.setText(foodinfoSmall.getName()+"  ￥："+foodinfoSmall.getPrice());
            MyBitmapUtil utils;
            utils = new MyBitmapUtil();
            utils.display(HttpUtils.url+foodinfoSmall.getIamge(), im_detail_food);

            bt_detail_food.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new OrderInfo(0, foodinfoSmall.getName(),
            Double.valueOf(  foodinfoSmall.getPrice()), 1,true));
                    getDialog().cancel();
                }
            });
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("FragmentFoodDetail", "视图销毁");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FragmentFoodDetail", "生命周期销毁");
    }
}
