package com.food.lmln.food.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.food.lmln.food.R;
import com.food.lmln.food.db.Constant;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.media.AudioTrack.SUCCESS;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.alipay.api.AlipayConstants.APP_ID;
import static com.alipay.api.AlipayConstants.CHARSET;
import static com.alipay.api.AlipayConstants.FORMAT;
import static com.alipay.api.AlipayConstants.SIGN_TYPE;

/**
 * Created by Weili on 2017/7/27.
 */

public class FragmentDialogPay extends DialogFragment {


 String    pid ="2088102169683910";
  String   appid ="2016080300153280";
//    # RSA私钥、公钥和支付宝公钥
     String       private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDjigbYvk/y6CwaIechD4bXsFBLp8vb9kvpDLXf+PUDYT2stRr+Qz8mFH+TEckYDknoIg2bsGtZZ7OklfsOIsyyT8BvTvo7hEWzTwQG8S9hnJktse9zXy31GfWOXhXIUkE6yMpdJPVIklulktt/Er8Kg62+vXd0A9EvDAOTGH7rKBjVYmdVKTiH2sag9IWdoao4SbLoz2ckOfB5TKSgwh1g0a3JJ3Deg0CTA5Ymo9q8A2fMjhRkai63E/piwgZudT64GdvW8CaffFMazUsmIKQS0ud/dpNuv902+KwyPoxZ4SKXa9HBJnakbdJOsvq1M0aWllvaZCRkOdM1mQ0+Er6JAgMBAAECggEBANl7I/AmduQFZaHUpcXDwW6G5zhouUEUuJmXvH7GxejlGL3qnFJKlCEhlo5LgZmsjpeBdOBKT1C0/c9iSwPH3z7PW28IXcRlOPmZnI/GFzOFKiOy3tu3LYYHudzJsKg3doFpSYKdwJQMz+wB4iojENZL5dVZ4sLQozAEm6YM08KOUt6guvF8erkBDg4o18SLG8zmfRAk0McE1Hql1H/3/LEWDINI+6cdCuUKXv/LUB2ZbS5CyAvfU3jmsu8ya3YDQsv/iPuUwM0fPJzAd0kAmAWLlo/eLvYrVpmgxmAxf9iwJzIpxK24wAfo+OBRnzyP8Tcb0IsHJm73N7Fj+n6ajwECgYEA/B+qa2iBBYBOHE/OpoiyWpTLV4jf0BdDfU4zC+oez8BdFNMfWYtz4OwgHKGc2UeTSMpg5GQ2wUQi/heyCzOgtwfyg1lAD2nQqnzK5MyZPontQeeViW9340dhJx5ZfWNVcDfE6bi4ZJ+m7owB7mUHhR5x4piy9fy4zMNicuFL8VUCgYEA5wmZRvuJFuO7t4dknwK+qO2SEFiKCU2cKlOZb8GBxaUWQA786fGYGE2s/RtyT22ZNQNrSCKQznQTqLOIc2GchUWPKfQeFzO1bWQFU2nYFUbWwQKTS7IlEk5PqB9fAn9XMIAlibXrPDE0erT7dpQdLtZQxsfXCSw0jr+FesTKaGUCgYEAxNlwdNYuTn5D/mTtYyc9oe2k6h5rKQzQ2+gPgRD1p514z4abZo20FvwlglVIonj47Q9eIVSBT9qEO8GCeK6BoAZredRYaPBkRIiU3ZlFWQfVn+xR2/GVOHYFy8YtdvH0xl40t4tLJP1mF8BmzZxCB9SidJpmuBruqqkul6WCZtUCgYAE51hAgHA9cyH12ELTeSxwy3IRNL/hSKtq3pFgJNxLRKC/RVIq6PzQbz2ftahsdSgOTv9E7a7jIuWT7BOWV1kKHxrVyh+lYGyWYMwYJCP496LxP8u+jI5q4Ayixd+vXuKL3fxt+tV+VpEFlbfMoYIpD1VxkqZGxJBtAzWjEHA3XQKBgEy7noYMt+6Y99Jjdwb6wNXFJLuoaSJ3qValTylBVfdg22gvO8MaIDMPPtYSMff33l6NNiO+3uzU3vb3ZZCJxiKoFqOAKJf29qOG7RNvWQWbk3li8H+LDXVft7VcH6JSyN2gGEJTCXsp1zkX+WXw5NiC9OhB/RBFHyzqiuBmWNr4";
   String  public_key ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyGlCQjXgXeA5A3OhQNeZPNsxD3TRuQ+NpKGSoJiZWxgtqf5kwTIT/ZYM4x6pnH8lTJjFD5q+Jo8VUfGhTR/6hsFkgsflSEzRC4ZoZVKm9tMv8cPdXOhfZi04nNDeuhKM4NkSHbe8RtPzgQCTE4BiimBkOeiQhAMpaWrclHcbOQEHUFMUjzd2aUhrxN4auv8Ws8xlmWFTFnoEeMKbdF8UpJdAlS8hr/5EY1HAoExxCe5ld4Z6pPKbV+sXAv68U1cwYfMfY1HCyyhlVTg1gE8enMA+STBCsJc9mfeC7nA1/CTC/qg2c5lHwhbwW60Ux5XGF6gwIai9SniNwftsYhfP8wIDAQAB";
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

    Handler mHandelr =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);  //点击外部不消失
        view = inflater.inflate(R.layout.fragement_dialog,container,false);
        im_pay_show = (ImageView) view.findViewById(R.id.im_pay_show);

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
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        new Thread(networkTask).start();
    }



    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {
//https://openapi.alipay.com/gateway.do?sign=MNV1APCvQcEXB2ksmcElX%2B8BTudqDqBErOcH9E5v1RvduNXVvtXWGAI63z7Q4IqvNNeXj595dGULe3TE17UlVbtJRTTknDbuaMpoVUR7HOvgih5qAdsEWXb2nqyGzV7OCm02OOQGj6lpFMEiPrwbdATqphnBlx9Iq%2FrdJMnd7hP6Q0vWj0ttdgfgfXi2JgzsgjrYXK4xlOpUYewdeDP%2BjNT5E0paNCyVAewImTgMrk2TIHBH1f38532jxZkowWi6YyJ23UBT3TDfsaS1qqzenEuhoE3gHIEE8Kz5qduuRwn7ZfNQXvpM51KYAOWYSFYFL%2FGVy%2F2ZMYIdZrJRc8sy1Q%3D%3D&timestamp=2017-07-28+12%3A02%3A03&charset=UTF-8&app_id=2016080300153280&method=alipay.trade.precreate&sign_type=RSA2&version=1.0&alipay_sdk=alipay-sdk-java-dynamicVersionNo&format=json

        @Override
        public void run() {
            test_trade_precreate();
            // TODO
            // 在这里进行 http request.网络请求相关操作

            AlipayClient alipayClient = new DefaultAlipayClient("http://openapi.alipay.com/gateway.do",appid,private_key,"json","UTF-8",public_key,"RSA2");
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setBizContent("{" +
                    "\"out_trade_no\":\"20150320010101001\"," +
                    "\"seller_id\":\"2088102146225135\"," +
                    "\"total_amount\":88.88," +
                    "\"discountable_amount\":8.88," +
                    "\"subject\":\"Iphone6 16G\"," +
                    "      \"goods_detail\":[{" +
                    "        \"goods_id\":\"apple-01\"," +
                    "\"goods_name\":\"ipad\"," +
                    "\"quantity\":1," +
                    "\"price\":2000," +
                    "\"goods_category\":\"34543238\"," +
                    "\"body\":\"特价手机\"," +
                    "\"show_url\":\"http://www.alipay.com/xxx.jpg\"" +
                    "        }]," +
                    "\"body\":\"Iphone6 16G\"," +
                    "\"operator_id\":\"yx_001\"," +
                    "\"store_id\":\"NJ_001\"," +
                    "\"disable_pay_channels\":\"pcredit,moneyFund,debitCardExpress\"," +
                    "\"enable_pay_channels\":\"pcredit,moneyFund,debitCardExpress\"," +
                    "\"terminal_id\":\"NJ_T_001\"," +
                    "\"extend_params\":{" +
                    "\"sys_service_provider_id\":\"2088511833207846\"" +
                    "    }," +
                    "\"timeout_express\":\"90m\"," +
                    "\"alipay_store_id\":\"2016052600077000000015640104\"" +
                    "  }");
            AlipayTradePrecreateResponse response = null;
            try {
                response = alipayClient.execute(request);
                if(response.isSuccess()){
//                    response.getQrCode()
                    Toast.makeText(getActivity(), "成功", Toast.LENGTH_SHORT).show();
                    Log.d("FragmentDialogPay", "成功");
                    System.out.println("调用成功");
                } else {
                    Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
                    Log.d("FragmentDialogPay", "是阿比");
                    System.out.println("调用失败");}
            } catch (AlipayApiException e) {
                e.printStackTrace();


            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "请求结果");
            msg.setData(data);
            mHandelr.sendMessage(msg);
        }
    };
    /**
     * 加载动画
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



    private void postAsynHttp() {
        OkHttpClient mOkHttpClient;
        mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("appid", appid)
                .add("private_key", private_key)
                .add("format", "json")
                .add("sign_type", "RSA2")
                .add("charset", "UTF-8")
                .build();
        Request request = new Request.Builder()
                .url("http://openapi.alipay.com/gateway.do")
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
                Log.i("wangshu", str);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
    // 测试当面付2.0生成支付二维码
    public void test_trade_precreate() {

    }
}
