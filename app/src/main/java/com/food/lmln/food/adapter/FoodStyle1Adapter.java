package com.food.lmln.food.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.food.lmln.food.R;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.utils.HttpUtils;
import com.food.lmln.food.utils.MyBitmapUtil;
import com.food.lmln.food.view.ScrollGridView;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.food.lmln.food.utils.ScreenUtils.getGridViewWeight;



/**
 *  @author Weli
 *  左侧适配器
 *  @time 2017-11-14  15:30
 *  @describe
 */
public class FoodStyle1Adapter extends BaseAdapter {
    LayoutInflater mInfnflater;
    private List<FoodinfoSmall> list;
    private Context mContext;
    private ScrollGridView gd_frgment1;
    private int[] viewSize;

    public FoodStyle1Adapter(List<FoodinfoSmall> list, Context mContext, ScrollGridView gd_frgment1, int[] viewsize) {
        this.list = list;
        this.mContext = mContext;
        this.gd_frgment1 = gd_frgment1;
        this.viewSize = viewsize;
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
            vieewHolder.lin_item = (LinearLayout) convertView.findViewById(R.id.lin_item);
            vieewHolder.im_item_image = (ImageView) convertView.findViewById(R.id.im_item_image);
            vieewHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            vieewHolder.tv_item_price = (TextView) convertView.findViewById(R.id.tv_item_price);
            vieewHolder.im_item_image.setLayoutParams(getGridViewWeight(mContext, list));
            convertView.setTag(vieewHolder);
        } else {
            vieewHolder = (VieewHolder) convertView.getTag();
        }
        int height;
        switch (list.size()) {
            case 1:

                height = 1;
                gd_frgment1.setNumColumns(1);
                break;
            case 7:
            case 8:
            case 9:
                height = 3;
                gd_frgment1.setNumColumns(3);
                break;
            default:
                height = 2;
                gd_frgment1.setNumColumns(2);
                break;
        }
        LinearLayout.LayoutParams linearParams;
        if (height < 3) {
            linearParams = new LinearLayout.LayoutParams(viewSize[0] - (height * 100) / height, (viewSize[1] - (height * 100)) / height);
        }    else {
            linearParams = new LinearLayout.LayoutParams(viewSize[0] / height, (viewSize[1] - (height * 50)) / height);
        }
        vieewHolder.lin_item.setLayoutParams(linearParams);
        LinearLayout.LayoutParams lin = new LinearLayout.LayoutParams((viewSize[0] - (height * 60) / height), ((viewSize[1] - (height * 150)) / height));
        vieewHolder.im_item_image.setLayoutParams(lin);

        vieewHolder.tv_item_name.setText(list.get(position).getName());
        vieewHolder.tv_item_price.setText("￥" + list.get(position).getPrice() + "");
        String url = HttpUtils.URL + list.get(position).getIamge();
        MyBitmapUtil utils;
        utils = new MyBitmapUtil();
        utils.display(url, vieewHolder.im_item_image);
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
//                EventBus.getDefault().post(new OrderInfo(0, list.get(position).getName(),
//                        Double.valueOf( list.get(position).getPrice()), 0,true));
                FoodinfoSmall info = new FoodinfoSmall(0, list.get(position).getName(), list.get(position).getDescribe(), list.get(position).getPrice(), list.get(position).getIamge());
                Gson gson = new Gson();
                String str = gson.toJson(info);
                mlistener.onFoodDetailClick(str);
            }
        });

        return convertView;
    }


    private class VieewHolder {
        private LinearLayout lin_item;
        private ImageView im_item_image;
        private TextView tv_item_name;
        private TextView tv_item_price;
    }


    /**
     * 定义一个接口，提供Activity使用
     */
    private OnFoodDetailListener mlistener;

    public interface OnFoodDetailListener {
        void onFoodDetailClick(String person);
    }

    public void setOnDetailListener(OnFoodDetailListener dialogListener) {
        this.mlistener = dialogListener;
    }


}




