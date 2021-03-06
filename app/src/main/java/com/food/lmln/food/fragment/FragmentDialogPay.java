package com.food.lmln.food.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.food.lmln.food.R;
import com.food.lmln.food.db.Constants;
import com.food.lmln.food.receiver.LocalBroadcastManager;
import com.food.lmln.food.utils.ExampleUtil;
import com.food.lmln.food.utils.HttpUtils;
import com.food.lmln.food.utils.JsonUtils;
import com.food.lmln.food.utils.ScreenUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kale.lib.time.AdvancedCountdownTimer;
import com.wang.avi.AVLoadingIndicatorView;

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
 *  @author Weli
 *  @time 2017-11-23  18:34
 *  @describe
 */
public class FragmentDialogPay extends DialogFragment {
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.food.lmln.food.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private String order_detail_value; //订单信息
    public String registration_id_key = "registration_id_key"; //极光id
    public String registration_id_value = "";
    public String product_id_key = "product_id_key";
    public String product_id_value = null;
    public String time_key = "time_key";

    public String order_key = "order_key";
    public String time_value = null;
    private String mParam1;
    private View view;
    private FrameLayout view_pay_bg;
    private ImageView im_pay_show;
    private LinearLayout lin_bg;
    private com.wang.avi.AVLoadingIndicatorView avloadingIndicatorView_BallClipRotatePulse;

    private TextView tv_pay_time; //倒计时
    public static boolean isForeground = false;
    OkHttpClient mOkHttpClient;
    private AdvancedCountdownTimer countdownTimer;
    private String deskNo;
    private String order_id;
    private String order_price;

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
            mParam1 = getArguments().getString(Constants.PAY_TYPE);
        }
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

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            avloadingIndicatorView_BallClipRotatePulse.setVisibility(View.GONE);
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.SEND_MSG_CODE1:
                    Bundle data = msg.getData();
                    String val = data.getString("value");
                    String order = data.getString("order");
                    int type = data.getInt("pay_type");
                    if (val != null && "error".equals(val)) {
                        startCustomCountDownTime(3, null, type);
                        Toast.makeText(getActivity(), R.string.error_htp, Toast.LENGTH_SHORT).show();
                    } else {
                        startCustomCountDownTime(90, order, type);
                        im_pay_show.setImageBitmap(generateBitmap(val, 500, 500));
                    }
                    break;
                case Constants.SEND_MSG_CODE2:
                    Bundle bundle1 = msg.getData();
                    String order_no = bundle1.getString("order_no");
                    Log.d("look", order_no);
                    postCirculation(order_no);
                    break;
                case Constants.SEND_MSG_CODE3:
                    Bundle bundle2 = msg.getData();
                    String query = bundle2.getString("query");
                    if (query != null) {
                        setCustomMsg(query);
                    }
                    break;
                default:
                    break;
            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //点击外部不消失
        getDialog().setCanceledOnTouchOutside(false);
        view = inflater.inflate(R.layout.fragement_dialog_pay, container, false);
        initView();
        return view;
    }


    /**
     * 初始化布局
     */
    private void initView() {
        im_pay_show = (ImageView) view.findViewById(R.id.im_pay_show);
        avloadingIndicatorView_BallClipRotatePulse = (AVLoadingIndicatorView) view.findViewById(R.id.avloadingIndicatorView_BallClipRotatePulse);
        lin_bg = (LinearLayout) view.findViewById(R.id.lin_bg);
        view_pay_bg = (FrameLayout) view.findViewById(R.id.view_pay_bg);
        tv_pay_time = (TextView) view.findViewById(R.id.tv_pay_time);
        slideToUp(view);
        initData();
    }

    /**
     * 初始化信息
     */
    private void initData() {
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    return true;
//                }
//                return false;
//            }
//        });
        registration_id_value = JPushInterface.getRegistrationID(getActivity());
//        product_id_value = "1.0";
        time_value = System.currentTimeMillis() + "";
        ScreenUtils.setMargins(im_pay_show, 50, 150, 50, 50);
        String type = null;
        String ordrNo = null;

        if (mParam1 != null) {
            Log.d("jpush", mParam1 + "dsadsa");
            String[] temp = mParam1.split("####");
            type = temp[0];
            ordrNo = temp[1];
            product_id_value = temp[2];
            order_detail_value = temp[3];
            deskNo = temp[4];
        }
        if (type != null && type.equals(Constants.ALI)) {
            String url = HttpUtils.ALI_PAY;
            view_pay_bg.setBackgroundResource(R.mipmap.pay_ali_bg);
            postAsynHttp(product_id_value, registration_id_value, time_value, ordrNo, url, Constants.PAY_TYEPE_ALI);
        } else {
            String url = HttpUtils.WX_PAY;
            view_pay_bg.setBackgroundResource(R.mipmap.pay_wx_bg);
            postAsynHttp(product_id_value, registration_id_value, time_value, ordrNo, url, Constants.PAY_TYEPE_WX);
        }
    }

    /**
     * "
     * 循环查询
     *
     * @param str OrderNO
     */
    private void postCirculation(String str) {
        String url = HttpUtils.WX_QUERY;
        mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(order_key, str)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = Constants.SEND_MSG_CODE3;
                Bundle data = new Bundle();
                data.putString("query", null);
                msg.setData(data);
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                JSONObject jsonObject;
                Log.d("FragmentDialogPay", str);
                try {
                    jsonObject = new JSONObject(str);
                    boolean result = jsonObject.getBoolean("result");
                    String money = jsonObject.getString("money");
                    if (result && money != null) {
                        Message msg = new Message();
                        msg.what = Constants.SEND_MSG_CODE3;
                        Bundle data = new Bundle();
                        data.putString("query", jsonObject.toString());

                        msg.setData(data);
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 网络请求
     *
     * @param mId   订单信息
     * @param Rid   本机极光id
     * @param Rtime 时间
     * @param url   访问地址
     */
    private void postAsynHttp(String mId, String Rid, String Rtime, String order, String url, final int type) {
        avloadingIndicatorView_BallClipRotatePulse.setVisibility(View.VISIBLE);
        Log.d(TAG, url);

        mOkHttpClient = new OkHttpClient();
        /**
         * 价格
         * 订单
         */
        order_id = "order_id";
        order_price = "order_price";
        RequestBody formBody = new FormBody.Builder()
                .add(order_price, mId)
                .add(registration_id_key, Rid)
                .add(order_id, order)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.d("FragmentDialogPay", e.getMessage());
//                Toast.makeText(getActivity(),  R.string.tip_net_fail, Toast.LENGTH_SHORT).show();
                Message msg = new Message();
                msg.what = Constants.SEND_MSG_CODE1;
                Bundle data = new Bundle();
                data.putString("value", "error");
                data.putInt("pay_type", type);
                msg.setData(data);
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                String url;
                String order = null;
                JSONObject josn;
                Log.d("FragmentDialogPay", str);
                try {
                    josn = new JSONObject(str);
                    boolean flag = josn.getBoolean("result");
                    if (flag) {
                        url = josn.getString("payUrl");
                        order = josn.getString("order_no");
                    } else {
                        url = "error";
                    }
                    Message msg = new Message();
                    msg.what = Constants.SEND_MSG_CODE1;
                    Bundle data = new Bundle();
                    data.putString("value", url);
                    data.putString("order", order);
                    data.putInt("pay_type", type);
                    msg.setData(data);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * loading animation
     * @param view
     */
    public static void slideToUp(View view) {
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


    /**
     * 付款成功退出Dialog动画
     *
     * @param view
     */
    public void closeScale(final View view) {
        final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setFillEnabled(true);
        view.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getDialog().cancel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 生成二维码
     *
     * @param content 上下文
     * @param width   宽
     * @param height  高
     * @return bitmap
     */
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

    /**
     * 注册信息（极光）
     */
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
                Log.d("jpush", extras);
                Log.d("jpush", extras);
                JSONObject json = null;
                String data = null;
                try {
                    json = new JSONObject(extras);
                    data = json.getString("data");

                    setCustomMsg(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }


            }
        }
    }

    /**
     * 收到信息打印
     *
     * @param msg json
     */
    private void setCustomMsg(String msg) {
        String json = JsonUtils.useJpushJson(msg, order_detail_value,deskNo);
        Log.d("aaaa", json);
        if (json != null) {
            onDialogListener.onDialogClick(json);
            closeScale(lin_bg);
        }
    }
    @Override
    public void onResume() {
        isForeground = true;

        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        if (null != countdownTimer) {
            countdownTimer.cancel();
        }
        Log.d(TAG, "exit");
        super.onDestroy();
    }

    /**
     * 开始计时 传入参数 time ，单位秒
     *
     * @param time 秒
     * @param str  订单号
     * @param type 类型
     */
    private void startCustomCountDownTime(long time, final String str, final int type) {
        countdownTimer = new AdvancedCountdownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished, int percent) {
                int time = Integer.parseInt(String.valueOf(millisUntilFinished / 1000));
                if (isAdded()) {
                    String sAgeFormat = getResources().getString(R.string.text_surplus_time);
                    String sFinalAge = String.format(sAgeFormat, time + "");
                    if (type == Constants.PAY_TYEPE_WX) {
                        if (time < 81 && time % 8 == 0 && str != null) {
                            Log.d("FragmentDialogPay", "time:" + time);
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("order_no", str);
                            msg.setData(bundle);
                            msg.what = Constants.SEND_MSG_CODE2;
                            handler.sendMessage(msg);
                        }
                    }
                    if (time < 30) {
                        tv_pay_time.setTextColor(getResources().getColor(R.color.colorAccen1t));
                    }
                    tv_pay_time.setText(sFinalAge);
                }
            }

            @Override
            public void onFinish() {
                closeScale(lin_bg);
            }
        };
        countdownTimer.start();
    }


    /**
     * 定义一个接口，提供Activity使用
     */
    OnDialogListener onDialogListener;

    public interface OnDialogListener {
        /**
         * 回传支付的订单
         * @param person
         */
        void onDialogClick(String person);
    }

    public void setOnDialogListener(OnDialogListener dialogListener) {
        this.onDialogListener = dialogListener;
    }
}
