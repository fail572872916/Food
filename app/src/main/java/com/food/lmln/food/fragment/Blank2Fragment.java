package com.food.lmln.food.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.food.lmln.food.R;
import com.food.lmln.food.adapter.PageWidgetAdapter;
import com.food.lmln.food.view.PageWidget;




public class Blank2Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private PageWidget page;
    private BaseAdapter adapter;

    private Integer[] imgs = { R.layout.fragment_layout1,R.layout.fragment_layout2, R.layout.fragment_layout3,
            R.layout.fragment_layout4, R.layout.fragment_layout5, R.layout.fragment_layout6, R.layout.fragment_layout7,
            R.layout.fragment_layout8, R.layout.fragment_layout9, R.layout.fragment_layout10, R.layout.fragment_layout11,
            R.layout.fragment_layout12};
//    private Integer[] imgs = { R.layout.fragment_layout1,R.layout.fragment_layout1,R.layout.fragment_layout1,R.layout.fragment_layout1,
//        R.layout.fragment_layout1,R.layout.fragment_layout1,R.layout.fragment_layout1,R.layout.fragment_layout1,R.layout.fragment_layout1,
//        R.layout.fragment_layout1,R.layout.fragment_layout1,R.layout.fragment_layout1,R.layout.fragment_layout1,R.layout.fragment_layout1};
    Handler mHandler;



    public Blank2Fragment() {
        // Required empty public constructor
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                page.setAdapter(adapter);

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank2, null);
        page = (PageWidget) view.findViewById(R.id.main_pageWidget);

        adapter = new PageWidgetAdapter(getActivity(), imgs);


       initView();
        return view;

    }

    private void initView() {
        new Thread(){
            @Override
            public void run() {
                super.run();

                mHandler.sendEmptyMessage(0);

            }
        }.start();
    }



}
