package com.food.lmln.food.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.food.lmln.food.R;

/**
 * Created by Weili on 2017/6/22.
 */

public class LauncherActivity extends Activity {
    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;

    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISH_LAUNCHERACTIVITY:
                    //璺宠浆鍒癕ainActivity锛屽苟缁撴潫褰撳墠鐨凩auncherActivity
                    Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 涓嶆樉绀虹郴缁熺殑鏍囬鏍忥紝淇濊瘉windowBackground鍜岀晫闈ctivity_main鐨勫ぇ灏忎竴鏍凤紝鏄剧ず鍦ㄥ睆骞曚笉浼氭湁閿欎綅锛堝幓鎺夎繖涓�琛岃瘯璇曞氨鐭ラ亾鏁堟灉浜嗭級
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 娉ㄦ剰锛氭坊鍔�3绉掔潯鐪狅紝浠ョ‘淇濋粦灞忎竴浼氬効鐨勬晥鏋滄槑鏄撅紝鍦ㄩ」鐩簲鐢ㄨ鍘绘帀杩�3绉掔潯鐪�
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.fragment_layout8);

        // 鍋滅暀3绉掑悗鍙戦�佹秷鎭紝璺宠浆鍒癕ainActivity
        mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 100);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // land
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // port
            }
        } catch (Exception ex) {
        }

    }
    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
}
