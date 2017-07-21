package com.food.lmln.food.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.food.lmln.food.R;
import com.food.lmln.food.adapter.PageWidgetAdapter;
import com.food.lmln.food.bean.FoodInfo;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.db.MysqlDb;
import com.food.lmln.food.view.PageWidget;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.food.lmln.food.db.Constant.PASSWORD;
import static com.food.lmln.food.db.Constant.SQLURL;
import static com.food.lmln.food.db.Constant.USERNAME;
import static com.food.lmln.food.db.Constant.send_msg_code1;
import static com.food.lmln.food.db.Constant.send_msg_code3;


public class Blank2Fragment extends Fragment {
    private List<FoodinfoSmall> simpleList = new ArrayList<FoodinfoSmall>();
    private List<FoodinfoSmall> personList= new ArrayList<FoodinfoSmall>();
    private List<FoodinfoSmall> foodList= new ArrayList<FoodinfoSmall>();

    private Connection conn; //Connection连接
    private int pageIndex=1; //当前页数;
    private int  pageSize=4;//每页显示的个数
    private int  pageNum;//每页显示的个数
    String  tableName;
    private PageWidget page;
    private BaseAdapter adapter;
    Handler mHandler;
    public Blank2Fragment() {
        // Required empty public constructor
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case send_msg_code1:


                        adapter = new PageWidgetAdapter(getActivity(), foodList);
                        page.setAdapter(adapter);
                        break;
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null) {
            tableName = bundle.getString("foodName");
        }
        View view = inflater.inflate(R.layout.fragment_blank2, null);
        page = (PageWidget) view.findViewById(R.id.main_pageWidget);

        initView();
        return view;
    }
    private void initView() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                foodList = MysqlDb.selectFood(conn, "select  * from  "+tableName+"");
                Log.d("Blank2Fragment", "foodList1:" + foodList);
                Message msg = new Message();
        msg.what = send_msg_code1;
        Bundle bundle = new Bundle();
        bundle.putSerializable("lookList", (Serializable) foodList);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
//                initFood(tableName);
            }
        }).start();
    }
//    private void initFood(String tableName) {
//        int pageCount;  //总页数
//        pageCount = (int) Math.ceil(foodList1.size() / (double) pageSize);
//        pageNum = (int) Math.ceil(pageCount / (double) pageSize);
//
//        for (int i = 1; i < pageNum + 1; i++) {
//            FoodInfo f = new FoodInfo();
//            f.setKey(i);
//            personList = MysqlDb.ByPageIndex(conn, tableName, pageIndex, pageSize);
//            f.setList(personList);
//            foodList.add(f);
//        }
//
//        Message msg = new Message();
//        msg.what = send_msg_code1;
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("lookList", (Serializable) foodList);
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
//    }

    @Override
    public void onResume() {
        super.onResume();
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
