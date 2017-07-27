package com.food.lmln.food.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.food.lmln.food.R;
import com.food.lmln.food.db.Constant;

/**
 * Created by Weili on 2017/7/27.
 */

public class FragmentDialogPay extends DialogFragment {

    private String mParam1;
    private View view;
    private ImageView im_pay_show;

    public static FragmentDialogPay newInstance() {
        Bundle args = new Bundle();
        FragmentDialogPay fragment = new FragmentDialogPay();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(Constant.PAY_TYPE);
        }
          Log.d("FragmentDialogPay", mParam1);
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.fragement_dialog,container,false);
        im_pay_show = (ImageView) view.findViewById(R.id.im_pay_show);

//        AnimationUtils.slideToUp(view);
        initView();
        return view;
    }

    private void initView() {
        if(mParam1.equals(Constant.ALI)){
            im_pay_show.setImageResource(R.mipmap.pay_ali_show);
        }else{
            im_pay_show.setImageResource(R.mipmap.pay_wechat);
        }
    }

}
