package com.food.lmln.food.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;


import com.food.lmln.food.R;
import com.food.lmln.food.activity.MainActivity;
import com.food.lmln.food.db.Constant;
import com.food.lmln.food.receiver.LocalBroadcastManager;
import com.food.lmln.food.utils.ExampleUtil;
import com.food.lmln.food.utils.HttpUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.greenrobot.eventbus.EventBus.TAG;


/**
 * Created by Weili on 2017/7/27.
 */

public class FragmentDialogPay extends DialogFragment {
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.food.lmln.food.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public String registration_id_key = "registration_id_key";
    public String registration_id_value = "";
    public String product_id_key = "product_id_key";
    public String product_id_value = "";
    public String time_key = "time_key";
    public String time_value = "";

    private String mParam1;
    private View view;
    private ImageView im_pay_show;
    public static boolean isForeground = false;

      OkHttpClient mOkHttpClient;
    private String finalUrl;

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

        registerMessageReceiver();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");

                im_pay_show.setImageBitmap(generateBitmap(val,500,500));



        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);  //点击外部不消失
        view = inflater.inflate(R.layout.fragement_dialog,container,false);
        im_pay_show = (ImageView) view.findViewById(R.id.im_pay_show);

        registerMessageReceiver();

        slideToUp(view);
        initView();
        return view;
    }
    private void initView() {
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    return true;
//                }
//                return false;
//            }
//        });
        if(mParam1.equals(Constant.ALI)){
            im_pay_show.setImageResource(R.mipmap.pay_ali_show);
        }else{
            im_pay_show.setImageResource(R.mipmap.pay_wechat);
            registration_id_value= JPushInterface.getRegistrationID(getActivity());
            product_id_value="1";
            time_value=System.currentTimeMillis()+"";
            Log.d("FragmentDialogPay", time_value);
            if (registration_id_value.isEmpty()) {
            } else if(product_id_value.isEmpty()) {
            }else if(time_value.isEmpty()){
            }else {
                postAsynHttp(product_id_value,registration_id_value,time_value);


            }
        }

    }

    /**
     * 进行网络请求
     */
    private void postAsynHttp( String mId,String Rid,String Rtime) {
        mOkHttpClient=new OkHttpClient();
        String url= HttpUtils.POSTWX+"Pay1?";
        Log.d("FragmentDialogPay", mId + registration_id_key);
        RequestBody formBody = new FormBody.Builder()
                .add(product_id_key, mId)
                .add(registration_id_key, Rid)
                .add(time_key, Rtime)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str = response.body().string();
                String url = null;
                Log.i("wangshu", str);
                JSONObject josn = null;
                try {
                    josn = new JSONObject(str);
                    url = josn.getString("data");
                    Log.d("FragmentDialogPay", url);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value",url);
                    msg.setData(data);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

     /* 加载动画
     * @param view
     */
    public static void slideToUp(View view){
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }


    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, filter);
    }
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }
    private void setCostomMsg(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        Log.d("FragmentDialogPay", msg);
      getDialog().dismiss();
    }
    @Override
    public void onResume() {
        isForeground = true;
        registerMessageReceiver();
        super.onResume();
    }
    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }



}
