package com.food.lmln.food.view;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.food.lmln.food.R;
import com.food.lmln.food.bean.DeskInfo;
import com.food.lmln.food.db.Constant;
import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.db.SqlHelper;
import com.food.lmln.food.fragment.FragmentDialogPay;
import com.food.lmln.food.utils.SystemUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Weili on 2017/7/3.
 * 设置ip
 */
public class DialogTablde {
    private static SQLiteDatabase db;
    static SqlHelper helper;
    static int isUpdate =0;
     static AlertDialog dialog;
    /**
     * 定义一个接口，提供Activity使用
     */
 public static OnDialogCloseListener mlistener;
    public   interface OnDialogCloseListener {


        void onDialogCloseClick();
    }

    public void setCloseListener(OnDialogCloseListener dialogListener) {
        this.mlistener = dialogListener;
    }

    public  static  void showDialog(final Context mContext) {
        ImageView iv_dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Window window =dialog.getWindow();
//        View view = View.inflate(mContext, R.layout.dialog_setting, null);
        window.setContentView( R.layout.dialog_setting);
//        builder.setCancelable(true);
        helper=DbManger.getInstance(mContext);
        db = helper.getWritableDatabase();
        final MaterialEditText tv_dialog_ip= (MaterialEditText) window.findViewById(R.id.tv_dialog_ip);//输入内容
        final   MaterialEditText tv_dialog_tesk= (MaterialEditText) window.findViewById(R.id.tv_dialog_tesk);//输入内容
        final   Button btn_cancel=(Button)window.findViewById(R.id.btn_cancel);//取消按钮
        final   Button btn_comfirm=(Button)window.findViewById(R.id.btn_comfirm);//确定按钮
        iv_dialog = (ImageView) window.findViewById(R.id.iv_dialog);
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

        //取消或确定按钮监听事件处理
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

                    mlistener.onDialogCloseClick();
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

    }



}
