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
import com.food.lmln.food.db.Constant;
import com.food.lmln.food.utils.MyBitmapUtil;


/**
 * Created by Administrator on 2017/8/23.
 */

public class FodDetailFragment extends DialogFragment {
    private View view;
    private ImageView im_detail_food;
    private TextView tv_detail_food;
    private Button bt_detail_food;
    private  String mParam1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(Constant.FOOD_DETAIL);
            Log.d("FodDetailFragment", mParam1);
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
        im_detail_food= (ImageView) view.findViewById(R.id.im_detail_food);
        tv_detail_food= (TextView) view.findViewById(R.id.tv_detail_food);
        bt_detail_food= (Button) view.findViewById(R.id.bt_detail_food);
        intData();
    }

    private void intData() {

        MyBitmapUtil utils;
        utils = new MyBitmapUtil();
        utils.display("http://pic.qjimage.com/bld018/high/bld180830.jpg",im_detail_food);

    }


}
