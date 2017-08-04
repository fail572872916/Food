package com.food.lmln.food.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.food.lmln.food.R;
import com.food.lmln.food.bean.FoodInfo;
import com.food.lmln.food.utils.HttpUtils;
import com.food.lmln.food.utils.MyBitmapUtil;


import java.util.List;


/**
 * Created by Weili on 2017/6/2.
 */

public class FoodStyle2Adapter extends BaseAdapter    {
    LayoutInflater mInfnflater;
    private List<FoodInfo> list;    //功能集合
    private Context mContext;
    private  int [] mInfo = new int[2];

    int height =mInfo[1];
    int weight=mInfo[0];



    public FoodStyle2Adapter(List<FoodInfo> list, Context mContext, int[] info) {
        this.list = list;
        this.mContext = mContext;
        this.mInfo = info;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FoodStyle2Adapter.VieewHolder vieewHolder;
        if (convertView == null) {
            vieewHolder = new FoodStyle2Adapter.VieewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_gv, null);
            vieewHolder.im_item_image = (ImageView) convertView.findViewById(R.id.im_item_image);
            vieewHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            vieewHolder.tv_item_price = (TextView) convertView.findViewById(R.id.tv_item_price);
            for (int i = 0; i < mInfo.length; i++) {
                Log.d("FoodStyle1Adapter", "getInfo[i]:" + mInfo[i]);

            }
            Log.d("FoodStyle1Adapter", "mInfo[0]:" + mInfo[0]/2);
            Log.d("FoodStyle1Adapter", "mInfo[0]:" + mInfo[1]/4);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( 300,280);
            vieewHolder.im_item_image.setLayoutParams(layoutParams);

            convertView.setTag(vieewHolder);
        } else {
            vieewHolder = (FoodStyle2Adapter.VieewHolder) convertView.getTag();
        }
//        vieewHolder.tv_item_name.setText(list.get(position).getName());
//        vieewHolder.tv_item_price.setText("￥"+list.get(position).getPrice()+"");
//
//        String url=Url+list.get(position).getIamge();
//        Log.d("FoodStyle1Adapter", url+"fdasf");
//
//        MyBitmapUtil utils;   utils = new MyBitmapUtil();
//        utils.display(url,vieewHolder.im_item_image);


        return convertView;
    }


    public class VieewHolder {

        public ImageView im_item_image;
        public TextView tv_item_name;
        public TextView tv_item_price;
    }









}




