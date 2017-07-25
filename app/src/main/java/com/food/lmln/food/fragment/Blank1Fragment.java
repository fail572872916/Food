package com.food.lmln.food.fragment;

import android.content.Context;
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
import android.widget.Toast;

import com.food.lmln.food.BuildConfig;
import com.food.lmln.food.R;
import com.food.lmln.food.adapter.SpecialCuisineAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Blank1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;


    private ViewPager vPager;
    private List<Fragment> list = new ArrayList<Fragment>();
    private SpecialCuisineAdapter adapter;


    private SpecialCuisineAdapter adapter1;
    Handler mHandler ;
    private String str;

    public Blank1Fragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank1, container, false);

        initView();


        return view;
    }

    private void initView() {
        vPager  = (ViewPager) view.findViewById(R.id.vp_fragment1);

        BlankFragment fragment5 = new BlankFragment();
        list.add(fragment5);
        adapter = new SpecialCuisineAdapter(getActivity().getSupportFragmentManager(), list);


        vPager.setAdapter(adapter);



        }









}
