package com.food.lmln.food.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.food.lmln.food.R;


import com.food.lmln.food.adapter.FoodStyle1Adapter;
import com.food.lmln.food.bean.FoodInfo;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.bean.OrderInfo;
import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.db.MysqlDb;
import com.food.lmln.food.utils.HttpUtils;
import com.food.lmln.food.utils.MyBitmapUtil;
import com.food.lmln.food.view.ScrollGridView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;

import java.util.List;


import static com.food.lmln.food.db.Constant.PASSWORD;
import static com.food.lmln.food.db.Constant.SQLURL;
import static com.food.lmln.food.db.Constant.USERNAME;
import static com.food.lmln.food.db.Constant.send_msg_code1;

import static com.food.lmln.food.utils.HttpUtils.url;
import static com.food.lmln.food.utils.ScreenUtils.getScreenHeight;
//组件宽高
public class Blank3Fragment extends Fragment {


    MyAdapter adapter; //适配器

    private View view;
    LinearLayout   rl;
    private ViewPager viewPager;
    private List<FoodinfoSmall> simpleList = new ArrayList<FoodinfoSmall>();
    private List<FoodinfoSmall> personList= new ArrayList<FoodinfoSmall>();
    private List<FoodinfoSmall> foodList1= new ArrayList<FoodinfoSmall>();
    List<FoodInfo> foodList=new ArrayList<FoodInfo>();
    private Connection conn; //Connection连接
    private int pageIndex=1; //当前页数;
    private int  pageSize=6;//每页显示的个数
    private int  pageNum;//每页显示的个数
    private String  imageUrl1;
    private String  imageUrl2;
    private String  imageUrl3;
    private String  imageUrl4;
    private String  imageUrl5;
    private String  imageUrl6;
    private String  loadUrl1;
    private String  loadUrl2;
    private String  loadUrl3;
    private String  loadUrl4;
    private String  loadUrl5;
    private String  loadUrl6;
    String tableName;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            foodList  = (List<FoodInfo>) bundle.getSerializable("lookList");
            adapter = new MyAdapter();
            adapter.notifyDataSetChanged();
            viewPager.setOffscreenPageLimit(0);
            viewPager.setAdapter(adapter);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank1, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.vp_fragment1);
        rl  = (LinearLayout) view.findViewById(R.id.f1);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null) {
            tableName = bundle.getString("foodName");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                foodList1 = MysqlDb.selectFood(conn, "select  * from  "+tableName+"");
                initFood(tableName);
            }
        }).start();
//        //显示传递来的数据
//        adapter = new MyAdapter();
//        adapter.notifyDataSetChanged();
//        viewPager.setOffscreenPageLimit(0);
//        viewPager.setAdapter(adapter);
        return view;
    }
     private void initFood(String tableName) {
         int pageCount;  //总页数
         pageCount = (int) Math.ceil(foodList1.size() / (double) pageSize);
         pageNum = (int) Math.ceil(pageCount / (double) pageSize);

         for (int i = 1; i < pageNum + 1; i++) {
             FoodInfo f = new FoodInfo();
             f.setKey(i);
             personList = MysqlDb.ByPageIndex(conn, tableName, pageIndex, pageSize);
             f.setList(personList);
             foodList.add(f);
         }

         Message msg = new Message();
         Bundle bundle = new Bundle();
         bundle.putSerializable("lookList", (Serializable) foodList);
         msg.setData(bundle);
         mHandler.sendMessage(msg);
     }
    /**
     *
     * 适配器  MyAdapter
     */
    class MyAdapter extends PagerAdapter {
        private String  bigName;
        private String smallName;
        private String   smallPrice;
        private String bigPrice;
        private ViewPager pager;

        //用于存储回收掉的View
        private List<WeakReference<LinearLayout>> viewList=new ArrayList<WeakReference<LinearLayout>>();  ;
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==  object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            LinearLayout view = (LinearLayout) object;
            container.removeView(view);
            viewList.add(new WeakReference<LinearLayout>(view));
        }
        @Override
        public int getCount() {
            return pageNum;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (pager == null) {
                pager = (ViewPager) container;
            }
            View view = null;
            ViewHolder viewHolder = null;
            if (view ==  null) {
                view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_blank5, null);
                viewHolder = new ViewHolder();
                viewHolder.im_big1 = (ImageView) view.findViewById(R.id.im_big1);
                viewHolder.im_big2 = (ImageView) view.findViewById(R.id.im_big2);
                viewHolder.im_big3 = (ImageView) view.findViewById(R.id.im_big3);
                viewHolder.im_big4 = (ImageView) view.findViewById(R.id.im_big4);
                viewHolder.im_big5 = (ImageView) view.findViewById(R.id.im_big5);
                viewHolder.im_big6 = (ImageView) view.findViewById(R.id.im_big6);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            simpleList= foodList.get(position).getList();
            MyBitmapUtil utils;   utils = new MyBitmapUtil();
            Log.d("aaaa", "simpleList:" + simpleList);
            Log.d("aaaa", simpleList.get(0).getIamge());
            for (int i = 0; i < 6; i++) {
                if(simpleList.size()>=1){
              imageUrl1 =simpleList.get(0).getIamge();
                    loadUrl1= HttpUtils.url+imageUrl1;

                    utils.display(loadUrl1,viewHolder.im_big1);
                }
                if(simpleList.size()>2){
              imageUrl2 =simpleList.get(1).getIamge();
                }
                if(simpleList.size()>3){
                    imageUrl2 =simpleList.get(2).getIamge();
                }
                if(simpleList.size()>4){
                    imageUrl3 =simpleList.get(3).getIamge();
                }
                if(simpleList.size()>5){
                    imageUrl4 =simpleList.get(4).getIamge();
                }
                 if(simpleList.size()>6){
                    imageUrl5 =simpleList.get(5).getIamge();
                }


                loadUrl2= HttpUtils.url+imageUrl2;
                loadUrl3= HttpUtils.url+imageUrl3;
                loadUrl4= HttpUtils.url+imageUrl4;
                loadUrl5= HttpUtils.url+imageUrl5;
                loadUrl6= HttpUtils.url+imageUrl6;

            }





//            utils.display(loadUrl1,viewHolder.im_big1);
//            utils.display(loadUrl2,viewHolder.im_big2);
//            utils.display(loadUrl3,viewHolder.im_big3);
//            utils.display(loadUrl4,viewHolder.im_big4);
//            utils.display(loadUrl5,viewHolder.im_big5);
//            utils.display(loadUrl6,viewHolder.im_big6);
//            big_imageUrl=Url+String.valueOf(big_img);
//            small_imageUrl=Url+String.valueOf(small_img);
//            Log.d("MyAdapter", big_imageUrl+"");
//            Log.d("MyAdapter", small_imageUrl+"");
//            utils.display(big_imageUrl,viewHolder.im_big);
//            utils.display(small_imageUrl,viewHolder.im_small);
//            viewHolder.tv_small_text.setText(smallName+"");
//            mAdapter =new FoodStyle1Adapter(apdaterList,getActivity(),gd_frgment1);
//            gd_frgment1.setAdapter(mAdapter);
            pager.addView(view,0);
//            viewHolder.im_small.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    EventBus.getDefault().post(new OrderInfo(0, bigName,Double.valueOf(bigPrice)
//                            , 0,true));
//                }
//            }); viewHolder.im_big.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    EventBus.getDefault().post(new OrderInfo(0, smallName,Double.valueOf(smallPrice)
//                            , 0,true));
//                }
//            });

            return view;
        }
    }
    private class ViewHolder {
        public ImageView im_big1;
        public ImageView im_big2;
        public ImageView im_big3;
        public ImageView im_big4;
        public ImageView im_big5;
        public ImageView im_big6;

    }

    @Override
    public void onDestroyView() {


        super.onDestroyView();

       loadUrl1="";
       loadUrl2="";
       loadUrl3="";
       loadUrl4="";
       loadUrl5="";
       loadUrl6="";
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null) {
            tableName = bundle.getString("foodName");

        }
        new Thread(new Runnable() {
            @Override
            public void run() {



                conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                foodList1 = MysqlDb.selectFood(conn, "select  * from  "+tableName+"");
                Log.d("aaaaaa", "foodList1:" + foodList1);
                initFood(tableName);
            }
        }).start();
    }


    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
