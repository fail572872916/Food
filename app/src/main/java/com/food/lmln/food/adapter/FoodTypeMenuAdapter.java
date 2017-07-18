package com.food.lmln.food.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.food.lmln.food.R;
import com.food.lmln.food.activity.MainActivity;
import com.food.lmln.food.bean.MenuButton;
import com.food.lmln.food.bean.OrderInfo;

import java.util.List;

import static com.food.lmln.food.utils.ScreenUtils.getScreenHeight;
import static com.food.lmln.food.utils.ScreenUtils.getScreenWidth;
import static com.food.lmln.food.utils.ScreenUtils.getStatusHeight;

/**
 * Created by Weili on 2017/6/2.
 * 菜单名适配器
 */

public class FoodTypeMenuAdapter extends BaseAdapter {

    LayoutInflater mInfnflater;
    private List<MenuButton> list;    //功能集合
    private Context mContext;

    public FoodTypeMenuAdapter(List<MenuButton> list, Context mContext) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        VieewHolder vieewHolder;
        if (convertView == null) {
            vieewHolder = new VieewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_menu_type, null);
            vieewHolder.bt_item_menu = (TextView) convertView.findViewById(R.id.bt_item_menu);
            convertView.setTag(vieewHolder);

        } else {
            vieewHolder = (VieewHolder) convertView.getTag();
        }


        int height = getScreenHeight(mContext);

        vieewHolder.bt_item_menu.setHeight((height -120)/( list.size()+1));
        vieewHolder.bt_item_menu.setGravity(Gravity.CENTER);
        vieewHolder.bt_item_menu.setText(list.get(position).getName() );


        return convertView;
    }

    public class VieewHolder {
        public TextView bt_item_menu;
    }
}
