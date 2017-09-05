package com.food.lmln.food.utils;

import android.util.Log;

import org.json.JSONArray;
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
    public static String useJosn(boolean rs, String cmd, JSONObject data,String Deskno,String clear) {

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
            jsonObject.put("clear", clear);
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
//{"money":"1","order":"D201709010026","pay_type":2,"result":true}
// [{"consumptionId":"D201709010025","date":"2017-09-01","deskNO":"4号桌","time":"13:23:15","foodName":"小炒肉","foodPrice":35.0,"foodCount":1.0},
// {"consumptionId":"D201709010026","date":"2017-09-01","deskNO":"4号桌","time":"14:32:45","foodName":"咕噜肉","foodPrice":45.0,"foodCount":1.0}]



        JSONObject jsonObject1= new JSONObject();
        JSONArray myJsonArray ;
        try {
            myJsonArray = new JSONArray(m1);

            JSONObject json= new JSONObject(js);

            jsonObject1.put("data",json);
            Log.d("JsonUtils", json+"——————————————————"+json.toString());
            jsonObject1.put("orderDetail",myJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


}
