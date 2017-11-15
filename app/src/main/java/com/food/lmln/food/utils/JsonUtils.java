package com.food.lmln.food.utils;
import android.text.TextUtils;
import android.util.Log;
import com.food.lmln.food.bean.ReceiveSocket;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * @author Administrator
 * @name CarControl
 * @class name：com.example.administrator.carcontrol.utils
 * @class describe
 * @time 2017-10-13 10:59
 * @change
 * @chang time
 * @class describe
 */
public class JsonUtils {
    /**
     * 发送的Socket json数据
     *
     * @param flag
     * @param ip
     * @param message
     * @param operationCode
     * @param heartBeat
     * @return
     */
    public static String initSend(String flag, String ip, JSONObject message, String operationCode, String heartBeat) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("flaglist", jsonArray);
            jsonObject.put("flag", flag);
            jsonObject.put("socket_ip", ip);
            jsonObject.put("message", message.toString());
            jsonObject.put("operationCode", operationCode);
            jsonObject.put("heartBeat", heartBeat);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 解析接收的东西
     *
     * @param json
     * @return String  message
     */
    public static String jsonResolve(String json) {
        Gson gson = new Gson();
        String message = null;
        ReceiveSocket receiveSocket = gson.fromJson(json, ReceiveSocket.class);
        if (!TextUtils.isEmpty(receiveSocket.getOperationCode())) {
                message = receiveSocket.getMessage();
        }
        return message;

    }

    public static String jsonResolveType(String json) {
        Gson gson = new Gson();
        String messageType =null;
        ReceiveSocket receiveSocket = gson.fromJson(json, ReceiveSocket.class);
        if (!TextUtils.isEmpty(receiveSocket.getOperationCode())) {
            messageType = receiveSocket.getOperationCode();
        }
        return messageType;

}
    /**
     * 接收 数据，打包成通用
     *
     * @param rs   t/f
     * @param data
     * @return jsonObject
     */
    public static JSONObject createJson(boolean rs, Object data, String deskNo) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1= new JSONObject();
        try {
            if(deskNo!=null){
                jsonObject1.put("desk_num_str",deskNo);
            }
            jsonObject.put("result", rs);
            jsonObject.put("package", data);
            jsonObject.put("extra", jsonObject1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    /**
     * 传入推送数据 返回 JsonObject
     * @param js
     * @return
     */
    public static String useJpushJson(String js, String m1) {
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
