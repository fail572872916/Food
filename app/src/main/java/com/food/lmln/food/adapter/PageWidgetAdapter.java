package com.food.lmln.food.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.food.lmln.food.R;
import com.food.lmln.food.bean.FoodInfo;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.bean.OrderInfo;
import com.food.lmln.food.utils.HttpUtils;
import com.food.lmln.food.utils.MyBitmapUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class PageWidgetAdapter extends BaseAdapter {
    private Context mContext;
    private int count;
    private LayoutInflater inflater;
    private List<FoodinfoSmall> foodList = new ArrayList<FoodinfoSmall>();
//    private final String[] arr;

    public PageWidgetAdapter(Context mContext, List<FoodinfoSmall> foodList) {
        this.mContext = mContext;
        this.foodList = foodList;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        List<String> list = new ArrayList<String>();
//        for (FoodinfoSmall foodinfoSmall : foodList) {
//            list.add(HttpUtils.Url+foodinfoSmall.getIamge());
//        }
//        int size = list.size();
//        //使用了第二种接口，返回值和参数均为结果
//        arr = (String[])list.toArray(new String[size]);
//        int  buzu=4- foodList.size()%4;
//if(buzu!=0 &&buzu!=4) {
//    String b[] = new String[buzu];
//    for (int i = 0; i < b.length; i++) {
//        b[i] = "http://b.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=92e00c9b8f5494ee8722081f15ce87c3/29381f30e924b899c83ff41c6d061d950a7bf697.jpg";
//    }
//    arr = new String[foodList.size() + b.length];
//    for (int i = 0; i < foodList.size(); i++) {
//        if (i < foodList.size()) {
//            arr[i] = arr[i];
//        } else {
//            arr[i] = b[-foodList.size() + i];
//        }
//
//    }
//}else {
//    arr=arr;
//
//}
        count = (int) Math.ceil(foodList.size() / 4.0);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        Log.d("PageWidgetAdapter", "count:" + count);
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewGroup layout;
        if (convertView == null) {
            layout = (ViewGroup) inflater.inflate(R.layout.item_layout, null);
        } else {
            layout = (ViewGroup) convertView;
        }
        setViewContent(layout, position);

        return layout;


    }


    private void setViewContent(ViewGroup group, final int position) {

        MyBitmapUtil utils;
        utils = new MyBitmapUtil();
        TextView text1 = (TextView) group.findViewById(R.id.tv_name01);
        TextView text2 = (TextView) group.findViewById(R.id.tv_name02);
        TextView text3 = (TextView) group.findViewById(R.id.tv_name03);
        TextView text4 = (TextView) group.findViewById(R.id.tv_name04);
        ImageView image1 = (ImageView) group.findViewById(R.id.im_item01);
        ImageView image2 = (ImageView) group.findViewById(R.id.im_item02);
        ImageView image3 = (ImageView) group.findViewById(R.id.im_item03);
        ImageView image4 = (ImageView) group.findViewById(R.id.im_item04);

        if (position * 4 < foodList.size()) {
            utils.display(HttpUtils.URL + foodList.get(position * 4).getIamge(), image1);
            text1.setText(foodList.get(position * 4).getName() + "    ￥" + foodList.get(position * 4).getPrice());
        } else {
            image1.setImageResource(R.mipmap.not_photo);

            text1.setText("");
        }
        if (position * 4 + 1 < foodList.size()) {
            utils.display(HttpUtils.URL + foodList.get(position * 4 + 1).getIamge(), image2);
            text2.setText(foodList.get(position * 4 + 1).getName() + "    ￥" + foodList.get(position * 4 + 1).getPrice());

        } else {
            image2.setImageResource(R.mipmap.not_photo);
            text2.setText("");
        }
        if (position * 4 + 2 < foodList.size()) {
            utils.display(HttpUtils.URL + foodList.get(position * 4 + 2).getIamge(), image3);
            text3.setText(foodList.get(position * 4 + 2).getName() + "    ￥" + foodList.get(position * 4 + 2).getPrice());
        } else {
            image3.setImageResource(R.mipmap.not_photo);
            text3.setText("");

        }
        if (position * 4 + 3 < foodList.size()) {
            utils.display(HttpUtils.URL + foodList.get(position * 4 + 3).getIamge(), image4);
            text4.setText(foodList.get(position * 4 + 3).getName() + "    ￥" + foodList.get(position * 4 + 3).getPrice());
        } else {
            image4.setImageResource(R.mipmap.not_photo);
            text4.setText("");

        }
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.im_item01:
                        if (position * 4 <= foodList.size()) {

                            EventBus.getDefault().post(new OrderInfo(0, foodList.get(position * 4).getName(),
                                    Double.valueOf(foodList.get(position * 4).getPrice()), 0, true));
                        }
                        break;
                        default:
                            break;

                }

            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((position * 4 + 1) < foodList.size()) {
                    EventBus.getDefault().post(new OrderInfo(0, foodList.get(position * 4 + 1).getName(),
                            Double.valueOf(foodList.get(position * 4 + 2).getPrice()), 0, true));
                }

            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((position * 4 + 2) <= foodList.size()) {
                    EventBus.getDefault().post(new OrderInfo(0, foodList.get(position * 4 + 2).getName(),
                            Double.valueOf(foodList.get(position * 4 + 2).getPrice()), 0, true));
                }

            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((position * 4 + 3) <= foodList.size()) {
                    EventBus.getDefault().post(new OrderInfo(0, foodList.get(position * 4 + 3).getName(),
                            Double.valueOf(foodList.get(position * 4 + 3).getPrice()), 0, true));
                }

            }
        });


    }


}
