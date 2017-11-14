package com.food.lmln.food.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.food.lmln.food.R;
import com.food.lmln.food.adapter.FoodStyle2Adapter;
import com.food.lmln.food.bean.FoodInfo;
import com.food.lmln.food.db.Constants;
import com.food.lmln.food.db.DbManger;

import java.util.List;


import static com.food.lmln.food.utils.ScreenUtils.getScreenHeight;


public class Blank4Fragment extends Fragment {
    private static final String TAG = "MainActivity2";
    @SuppressLint("HandlerLeak")
    public Handler handlerMain = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.SEND_MSG_CODE1:

                    break;
//                case send_msg_code2:
//
//                    lv_main_order.setAdapter(new FoodOrderAdapter(list_order, MainActivity.this));
//                    break;
                default:
                    break;
            }

        }
    };


    GridView gd_frgment1;
    //图片缓存用来保存GridView中每个Item的图片，以便释放
    TextView tv_small_text;
    ImageView ib_small;
    ImageView ib_big;
    private TextView textview_show_prompt = null;
    private GridView gridview_test = null;
    private List<String> mList = null;
    private View view;
    private FoodStyle2Adapter mAdapter;
    private List<FoodInfo> list;
    private String text;
    private String big_imageUrl;
    private String small_imageUrl;
    private int[] getInfo = new int[2];
    private int heiget;
    private DbManger dbManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plus_one, container, false);
        initView();
//        initData();

        return view;
    }

    /**
     * 初始化组件
     */
//    private void initData() {
//        List<FoodinfoSmall> list= new ArrayList<FoodInfo>();
//        getLayoutInfo();
//        dbManager = new DbManger(getActivity());
//        list = dbManager.getAllFoodInfo();
//        List<FoodInfo> subList = list.subList(11, 17);    //获取子列表
//
//
//        mAdapter = new FoodStyle2Adapter(subList, getActivity(),getInfo);
//        gd_frgment1.setAdapter(mAdapter);
//        handlerMain.sendEmptyMessage(send_msg_code1);
//        List<FoodInfo> bigList = list.subList(8, 10);
//
//
//        MyBitmapUtil utils;   utils = new MyBitmapUtil();
//        String urlBig = null;
//        String urlSmall = null;
//        String tesxtSmall = null;
//        for (int i = 0; i < bigList.size(); i++) {
//            urlBig=  bigList.get(0).getIamge();
//            urlSmall= bigList.get(1).getIamge();
//            tesxtSmall=bigList.get(1).getName();
//        }
//        String url=Url+urlBig;
//        String url1=Url+urlSmall;
//        Log.d(TAG, url);
//        Log.d(TAG, url1);
//        utils.display(url,ib_big);
//        utils.display(url1,ib_small);
//        tv_small_text.setText(tesxtSmall+"");
//
//    }
    private void initView() {
        gd_frgment1 = (GridView) view.findViewById(R.id.gd_frgment1);
//        ib_big = (ImageView) view.findViewById(R.id.im_big);
//        ib_small = (ImageView) view.findViewById(R.id.im_small);
//        tv_small_text = (TextView) view.findViewById(R.id.tv_small_text);

    }


    /**
     * 获取组件宽高
     */
    private void getLayoutInfo() {

        heiget = getScreenHeight(getActivity());
        ViewTreeObserver vto2 = gd_frgment1.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gd_frgment1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                getInfo[0] = gd_frgment1.getWidth();
                getInfo[1] = heiget;
//                for (int i = 0; i < getInfo.length; i++) {
//                    Log.d(TAG, "getInfo[i]:" + getInfo[i]);
//                }

            }
        });

    }
}
