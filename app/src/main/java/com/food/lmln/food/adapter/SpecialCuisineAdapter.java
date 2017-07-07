package com.food.lmln.food.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Weili on 2017/6/7.
 * 特色菜适配器
 */

public class SpecialCuisineAdapter extends FragmentStatePagerAdapter {
    private List<Fragment>list;

    public SpecialCuisineAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        // TODO Auto-generated constructor stub
        list=fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 :  list.size();
    }
}