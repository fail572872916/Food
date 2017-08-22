package com.food.lmln.food.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.food.lmln.food.R;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.bean.OrderInfo;
import com.food.lmln.food.callback.ClothAddCallback;
import com.food.lmln.food.utils.HttpUtils;
import com.food.lmln.food.utils.MyBitmapUtil;
import com.food.lmln.food.utils.ScreenUtils;
import com.food.lmln.food.view.ScrollGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import static com.food.lmln.food.utils.ScreenUtils.getGridViewWeight;


/**
 * Created by Weili on 2017/6/2.
 */

public class FoodStyle1Adapter extends BaseAdapter    {
    LayoutInflater mInfnflater;
    private List<FoodinfoSmall> list;    //功能集合
    private Context mContext; //上下文
    private ClothAddCallback callback; //回调接口

    ScrollGridView gd_frgment1;

    public FoodStyle1Adapter(List<FoodinfoSmall> list, Context mContext, ScrollGridView gd_frgment1) {
        this.list = list;
        this.mContext = mContext;
        this.gd_frgment1 = gd_frgment1;
    }

//    public FoodStyle1Adapter(List<FoodInfo> list, Context mContext, ClothAddCallback callback) {
//        this.list = list;
//        this.mContext = mContext;
//        this.callback = callback;
//    }

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
    public View getView(final int position, View convertView, ViewGroup parent) {
         VieewHolder vieewHolder;
        if (convertView == null) {
            vieewHolder = new VieewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_gv, null);
            vieewHolder.im_item_image = (ImageView) convertView.findViewById(R.id.im_item_image);
            vieewHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            vieewHolder.tv_item_price = (TextView) convertView.findViewById(R.id.tv_item_price);
            vieewHolder.im_item_image.setLayoutParams(getGridViewWeight(mContext,list));
            convertView.setTag(vieewHolder);
        } else {
            vieewHolder = (VieewHolder) convertView.getTag();
        }
        int height = ScreenUtils.getScreenHeight(mContext);
        vieewHolder.tv_item_name.setText(list.get(position).getName());
        vieewHolder.tv_item_price.setText("￥"+list.get(position).getPrice()+"");
        String url= HttpUtils.url+list.get(position).getIamge();

        MyBitmapUtil utils;   utils = new MyBitmapUtil();
        utils.display(url,vieewHolder.im_item_image);
//        vieewHolder.im_item_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (CommonUtils.isFastDoubleClick()) {
//                     Toast.makeText(mContext, "请慢点", Toast.LENGTH_SHORT).show();
//                     return;
//                 }else {
//
//                    callback.updateRedDot(vieewHolder.im_item_image, position);
//                    notifyDataSetChanged();
//                }            }
//        });
        gd_frgment1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new OrderInfo(0, list.get(position).getName(),
                        Double.valueOf( list.get(position).getPrice()), 0,true));

            }
        });

            return convertView;
        }


    public class VieewHolder {

            public ImageView im_item_image;
            public TextView tv_item_name;
            public TextView tv_item_price;
        }






}




