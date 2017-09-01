package com.food.lmln.food.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Weili on 2017/8/16.
 *
 * @author Weli
 * @version 1.0.0
 * @Josn工具类
 */

public class JsonUtils {

    /**
     * 接收 数据，打包成通用
     *
     * @param rs   t/f
     * @param cmd
     * @param data
     * @return jsonObject
     */
    public static String useJosn(boolean rs, String cmd, JSONObject data,String Deskno) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1= new JSONObject();
        try {
            if(Deskno!=null){
                jsonObject1.put("desk_num_str",Deskno);
            }
            jsonObject.put("result", rs);
            jsonObject.put("orderInstruct", cmd);
            jsonObject.put("package", data.toString());
            jsonObject.put("extra", jsonObject1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 传入推送数据 返回 JsonObject
     * @param js
     * @return
     */
    public static String useJpushJosn(String js,String m1) {
//{"data":"{\"order\":\"20170816125005\",\"pay_type\":1,\"status\":true,\"money\":\"1.00\"}"}
        Log.d("JsonUtils", js);
        JSONObject jsonObject= null;
        JSONObject jsonObject1= new JSONObject();
        String json =null;
        try {
            jsonObject = new JSONObject(js);
            json =jsonObject.getString("data");
            jsonObject1.put("data",json);
            jsonObject1.put("orderDetail",m1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JsonUtils", "jsonObject1:" + jsonObject1);
        return jsonObject1.toString();
    }


}
