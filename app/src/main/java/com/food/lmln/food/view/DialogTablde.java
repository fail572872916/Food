package com.food.lmln.food.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.food.lmln.food.R;
import com.food.lmln.food.activity.MainActivity;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Weili on 2017/7/3.
 */

public class DialogTablde {
    Context mContext;
    public void showDialog(final Context mContext) {
        ImageView iv_dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_setting, null);



        builder.setView(view);
        builder.setCancelable(true);

        final MaterialEditText tv_dialog_ip= (MaterialEditText) view.findViewById(R.id.tv_dialog_ip);//输入内容
        MaterialEditText tv_dialog_tesk= (MaterialEditText) view.findViewById(R.id.tv_dialog_tesk);//输入内容
        iv_dialog = (ImageView) view.findViewById(R.id.iv_dialog);
        Glide.with(mContext).load(R.drawable.fab_add)
                .bitmapTransform(new BlurTransformation(mContext, 25), new CenterCrop(mContext))
                .into(iv_dialog);
        Button btn_cancel=(Button)view
                .findViewById(R.id.btn_cancel);//取消按钮
        Button btn_comfirm=(Button)view
                .findViewById(R.id.btn_comfirm);//确定按钮
        //取消或确定按钮监听事件处理
        final AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }
}
