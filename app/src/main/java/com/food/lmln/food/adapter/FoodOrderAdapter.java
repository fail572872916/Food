package com.food.lmln.food.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.food.lmln.food.R;
import com.food.lmln.food.bean.MenuButton;
import com.food.lmln.food.bean.OrderInfo;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Weili on 2017/6/2.
 */

public class FoodOrderAdapter extends BaseAdapter {

    LayoutInflater mInfnflater;
    private List<OrderInfo> list;    //功能集合
    private Context mContext;

    public FoodOrderAdapter(List<OrderInfo> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        mInfnflater.from(mContext);
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
        VieewHolder vieewHolder;
        if (convertView == null) {
            vieewHolder = new VieewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_order_info, null);
            vieewHolder.tv_item_id = (TextView) convertView.findViewById(R.id.tv_item_id);
            vieewHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            vieewHolder.tv_item_count_price = (TextView) convertView.findViewById(R.id.tv_item_count_price);
            vieewHolder.tv_item_count_count = (TextView) convertView.findViewById(R.id.tv_item_count_count);
            convertView.setTag(vieewHolder);


        } else {
            vieewHolder = (VieewHolder) convertView.getTag();

        }

        vieewHolder.tv_item_id.setText(list.get(position).getId()+"");
        vieewHolder.tv_item_name.setText(list.get(position).getName());

        /**
         *
         */
        double  orderPrice = (double) list.get(position).getPrice();
        int orderCount = (int) list.get(position).getCount();
        String  x= "x";
        String text = String.format(mContext.getResources().getString(R.string.order_item_text), x,orderCount);
        int index[] = new int[2];

        index[0] = text.indexOf(x);
        index[1] = text.indexOf(String.valueOf(orderCount));


        SpannableStringBuilder style = new SpannableStringBuilder(text);

        style.setSpan(new RelativeSizeSpan(0.6f), index[0],index[0]+x.length(),
                 Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        style.setSpan(new ForegroundColorSpan(Color.parseColor("#00bbfa")),
                index[1], index[1]+String.valueOf(orderCount).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        vieewHolder.tv_item_count_price.setText( orderPrice+"");
        vieewHolder.tv_item_count_count.setText(style);
        return convertView;
    }

        public class VieewHolder {
        public TextView tv_item_id;
        public TextView tv_item_name;
        public TextView tv_item_count_price;
        public TextView tv_item_count_count;
    }

}
