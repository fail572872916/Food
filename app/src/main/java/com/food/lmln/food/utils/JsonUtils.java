package com.food.lmln.food.utils;

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
    public static String useJosn(boolean rs, String cmd, JSONObject data) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("result", rs);
            jsonObject.put("orderInstruct", cmd);
            jsonObject.put("data", data);
            jsonObject.put("extra", "null");
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
    public static String useJpushJosn(String js) {
//{"data":"{\"order\":\"20170816125005\",\"pay_type\":1,\"status\":true,\"money\":\"1.00\"}"}
        JSONObject jsonObject= null;
        String json =null;
        try {
            jsonObject = new JSONObject(js);
            json =jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


}