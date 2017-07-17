package com.food.lmln.food.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;


import android.widget.ImageView;
import android.widget.TextView;

import com.food.lmln.food.R;


import com.food.lmln.food.adapter.FoodStyle1Adapter;
import com.food.lmln.food.bean.FoodInfo;
import com.food.lmln.food.bean.OrderInfo;
import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.utils.MyBitmapUtil;
import com.food.lmln.food.view.ScrollGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import java.util.List;




import static com.food.lmln.food.db.Constant.send_msg_code1;

import static com.food.lmln.food.utils.HttpUtils.Url;
import static com.food.lmln.food.utils.ScreenUtils.getScreenHeight;
//组件宽高
public class Blank3Fragment extends Fragment {




    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank5, container, false);


        return view;
    }





}
