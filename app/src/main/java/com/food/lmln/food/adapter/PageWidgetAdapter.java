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

import com.food.lmln.food.R;
import com.food.lmln.food.bean.FoodInfo;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.utils.HttpUtils;
import com.food.lmln.food.utils.MyBitmapUtil;

import java.util.ArrayList;
import java.util.List;


public class PageWidgetAdapter extends BaseAdapter {
    private Context mContext;
    private int count;
    private LayoutInflater inflater;
    private List<FoodinfoSmall> foodList= new ArrayList<FoodinfoSmall>();
//    private final String[] arr;
    private final String[] arr;
    public PageWidgetAdapter(Context mContext, List<FoodinfoSmall> foodList) {
        this.mContext = mContext;
        this.foodList = foodList;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<String> list = new ArrayList<String>();
        for (FoodinfoSmall foodinfoSmall : foodList) {
            list.add(HttpUtils.Url+foodinfoSmall.getIamge());
        }
        int size = list.size();
        //使用了第二种接口，返回值和参数均为结果
        arr = (String[])list.toArray(new String[size]);
//        int  buzu=4- arr.length%4;
//if(buzu!=0 &&buzu!=4) {
//    String b[] = new String[buzu];
//    for (int i = 0; i < b.length; i++) {
//        b[i] = "http://b.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=92e00c9b8f5494ee8722081f15ce87c3/29381f30e924b899c83ff41c6d061d950a7bf697.jpg";
//    }
//    arr = new String[arr.length + b.length];
//    for (int i = 0; i < arr.length; i++) {
//        if (i < arr.length) {
//            arr[i] = arr[i];
//        } else {
//            arr[i] = b[-arr.length + i];
//        }
//
//    }
//}else {
//    arr=arr;
//
//}
        count = (int) Math.ceil(arr.length/4.0);

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
        return arr[position];
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
        if(convertView == null) {
            layout = (ViewGroup) inflater.inflate(R.layout.item_layout, null);
        } else {
            layout = (ViewGroup) convertView;
        }
        setViewContent(layout, position);

        return layout;


    }


    private void setViewContent(ViewGroup group, int position) {

        MyBitmapUtil utils;   utils = new MyBitmapUtil();
        TextView text = (TextView) group.findViewById(R.id.item_layout_leftText);
        TextView    text1 = (TextView) group.findViewById(R.id.item_layout_rightText);
        ImageView image1 = (ImageView) group.findViewById(R.id.im_item01);
        ImageView image2 = (ImageView) group.findViewById(R.id.im_item02);
        ImageView image3 = (ImageView) group.findViewById(R.id.im_item03);
        ImageView image4 = (ImageView) group.findViewById(R.id.im_item04);



//        utils.display( arr[position * 2 + 1], image4);
//        utils.display(arr[position * 2 + 2], image3);
//
//        utils.display(arr[position * 2 + 3], image4);

        if(position*2<=arr.length){
            utils.display( arr[position*2], image1);
        }
        else{
          image1.setBackgroundResource(R.mipmap.ic_record_voice_over_black_48dp);
        }
        if(position*2+1<=arr.length){
            utils.display( arr[position*2+1], image2);
        }
        else{
            image2.setBackgroundResource(R.mipmap.ic_record_voice_over_black_48dp);
        }
           if(position*2+2<=arr.length){
            utils.display( arr[position*2+2], image3);
        }
        else{
          image3.setBackgroundResource(R.mipmap.ic_record_voice_over_black_48dp);
        }
         if(position*2+3<=arr.length){
            utils.display( arr[position*2+3], image4);
        }
        else{
          image4.setBackgroundResource(R.mipmap.ic_record_voice_over_black_48dp);
        }

        

}



}
