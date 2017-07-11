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

    private static final String TAG = "MainActivity";
    public Handler handlerMain = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case send_msg_code1:

                    break;
//                case send_msg_code2:
//
//                    lv_main_order.setAdapter(new FoodOrderAdapter(list_order, MainActivity.this));
//                    break;
            }
        }
    };

    ScrollGridView gd_frgment1;
    //图片缓存用来保存GridView中每个Item的图片，以便释放
    TextView tv_small_text;
    ImageView ib_small;
    ImageView ib_big;
    private TextView textview_show_prompt = null;
    private ScrollGridView gridview_test = null;
    private List<String> mList = null;
    private View view;
    private FoodStyle1Adapter mAdapter;
    private List<FoodInfo> list;
    private String text;
    private String big_imageUrl;
    private String small_imageUrl;

    private DbManger dbManager;
    private List<FoodInfo> subListGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plus_one, container, false);
        initView();
//    initData();

        return view;
    }
    /**
     * 初始化组件
     */
//    private void initData() {
//        List<FoodInfo> list= new ArrayList<FoodInfo>();
//
//        dbManager = new DbManger(getActivity());
//        list = dbManager.getAllFoodInfo();
//        //获取子列表
//        subListGridView = list.subList(2, 8);
//        mAdapter = new FoodStyle1Adapter(subListGridView, getActivity());
//        gd_frgment1.setAdapter(mAdapter);
//        handlerMain.sendEmptyMessage(send_msg_code1);
//        List<FoodInfo> bigList = list.subList(0, 2);
//
//
//        MyBitmapUtil utils;   utils = new MyBitmapUtil();
//
//        for (int i = 0; i < bigList.size(); i++) {
//            big_imageUrl=  bigList.get(0).getIamge();
//            small_imageUrl= bigList.get(1).getIamge();
//            text=bigList.get(1).getName();
//        }
//        big_imageUrl=Url+big_imageUrl;
//        small_imageUrl=Url+small_imageUrl;
//        utils.display(big_imageUrl,ib_big);
//        utils.display(small_imageUrl,ib_small);
//        tv_small_text.setText(text+"");
//
//    }

    private void initView() {
        gd_frgment1 = (ScrollGridView) view.findViewById(R.id.gd_frgment1);
        ib_big = (ImageView) view.findViewById(R.id.im_big);
        ib_small = (ImageView) view.findViewById(R.id.im_small);
        tv_small_text = (TextView) view.findViewById(R.id.tv_small_text);


        AnimationSet set = new AnimationSet(false);
        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(500);
        set.addAnimation(animation);

        animation = new TranslateAnimation(1, 13, 10, 50);
        animation.setDuration(300);
        set.addAnimation(animation);

//        animation = new RotateAnimation(30,10);
//        animation.setDuration(300);
//        set.addAnimation(animation);

//        animation = new ScaleAnimation(5,0,2,0);
//        animation.setDuration(300);
//        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 1);

        gd_frgment1.setLayoutAnimation(controller);
//        gd_frgment1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                EventBus.getDefault().post(new OrderInfo(0,subListGridView.get(position).getName(),
//                        subListGridView.get(position).getPrice(),0));
//            }
//        });

    }





}
