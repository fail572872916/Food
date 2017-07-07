package com.food.lmln.food.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import com.food.lmln.food.R;


public class PageWidgetAdapter extends BaseAdapter {
    private Context mContext;
    private int count;
    private LayoutInflater inflater;

    private Integer[] imgs ;

    public PageWidgetAdapter(Context context,Integer[] imgs) {
        mContext = context;
        this.imgs = imgs;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        count = (int) Math.ceil(imgs.length/2.0);

    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return imgs[position];
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
        FrameLayout image = (FrameLayout) group.findViewById(R.id.item_layout_leftImage);

        FrameLayout	image2 = (FrameLayout) group.findViewById(R.id.item_layout_rightImage);
//        ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT
        image.addView(View.inflate(mContext,imgs[position*2], null));
        image2.addView(View.inflate(mContext,imgs[position*2+1],null));
}



}
