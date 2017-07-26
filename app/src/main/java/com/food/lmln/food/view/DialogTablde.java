package com.food.lmln.food.view;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.food.lmln.food.R;
import com.food.lmln.food.activity.MainActivity;
import com.food.lmln.food.bean.DeskInfo;
import com.food.lmln.food.db.Constant;
import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.db.SqlHelper;
import com.food.lmln.food.utils.SystemUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Weili on 2017/7/3.
 */

public class DialogTablde {


    private SQLiteDatabase db;
    SqlHelper helper;
    int isUpdate =0;
    public void showDialog(final Context mContext) {
        ImageView iv_dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_setting, null);
        builder.setView(view);
        builder.setCancelable(true);
        helper=DbManger.getInstance(mContext);
        db = helper.getWritableDatabase();

        final MaterialEditText tv_dialog_ip= (MaterialEditText) view.findViewById(R.id.tv_dialog_ip);//输入内容
        final   MaterialEditText tv_dialog_tesk= (MaterialEditText) view.findViewById(R.id.tv_dialog_tesk);//输入内容
        iv_dialog = (ImageView) view.findViewById(R.id.iv_dialog);

            int  set=     DbManger.getCountPerson(db, Constant.DESK_INFO);
            if(set>0){
                db = helper.getWritableDatabase();
            List<DeskInfo> li=    DbManger.selectDeskInfo(db, Constant.DESK_INFO);
                    tv_dialog_ip.setText(li.get(0).getLocal_ip());
                tv_dialog_tesk.setText(li.get(0).getLocal_desk());
                isUpdate=1;
            }


        Glide.with(mContext).load(R.color.transparent)
                .bitmapTransform(new BlurTransformation(mContext, 150), new CenterCrop(mContext))
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
                db = helper.getWritableDatabase();
                String serverIp=tv_dialog_ip.getText().toString();
                String  tabDesk=tv_dialog_tesk.getText().toString();
                if(serverIp.equals("")||serverIp==""){
                    Toast.makeText(mContext,R.string.tip_ip , Toast.LENGTH_SHORT).show();
                }else if(tabDesk.equals("")||tabDesk==""){
                    Toast.makeText(mContext,  R.string.tip_desk_no, Toast.LENGTH_SHORT).show();
                }else if(SystemUtils.isboolIp(serverIp)==false){
                    Toast.makeText(mContext,  R.string.tip_check_ip, Toast.LENGTH_SHORT).show();
                }
                else {
                    DbManger.insertIP(db,serverIp,tabDesk,isUpdate);
                    dialog.dismiss();
                }



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
