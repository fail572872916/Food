package com.food.lmln.food.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.food.lmln.food.R;
import com.food.lmln.food.adapter.PageWidgetAdapter;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.db.Constants;
import com.food.lmln.food.db.MysqlDb;
import com.food.lmln.food.view.PageWidget;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.food.lmln.food.db.Constants.PASSWORD;
import static com.food.lmln.food.db.Constants.SQLURL;
import static com.food.lmln.food.db.Constants.USERNAME;

public class Blank2Fragment extends Fragment {

    private List<FoodinfoSmall> foodList = new ArrayList<FoodinfoSmall>();
    private Connection conn; //Connection连接

    String tableName;
    private PageWidget page;
    private BaseAdapter adapter;
    Handler mHandler;
    private View view;

    @SuppressLint("HandlerLeak")
    public Blank2Fragment() {
        // Required empty public constructor
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constants.SEND_MSG_CODE1:
                        adapter = new PageWidgetAdapter(getActivity(), foodList);
                        page.setAdapter(adapter);
                        break;
                    default:
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
        if (bundle != null) {
            tableName = bundle.getString("foodName");
        }
        view = inflater.inflate(R.layout.fragment_blank2, container, false);
        page = (PageWidget) view.findViewById(R.id.main_pageWidget);

        initView();
        return view;
    }

    private void initView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = MysqlDb.openConnection(SQLURL, USERNAME, PASSWORD);
                foodList = MysqlDb.selectFood(conn, "select  * from  " + tableName + "");
                Log.d("Blank2Fragment", "foodList1:" + foodList);
                Message msg = new Message();
                msg.what = Constants.SEND_MSG_CODE1;
                Bundle bundle = new Bundle();
                bundle.putSerializable("lookList", (Serializable) foodList);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
//                initFood(tableName);
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
