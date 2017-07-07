package com.food.lmln.food.utils;

/**
 * Created by Weili on 2017/6/15.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final String YAN="isgu&*%>qq:378592175(&378592175@qq.com)";


//    public static String encode(String password){
//        try {
//            password=YAN+password;
//            StringBuffer sb=new StringBuffer();
//            MessageDigest digest=MessageDigest.getInstance("md5");
//            byte[] bytes=digest.digest(password.getBytes());
//            for (byte b:bytes){
//                int n=b&0XFF;
//                String s=Integer.toHexString(n);
//                if (s.length()==1){
//                    sb.append("0"+s);
//                }else {
//                    sb.append(s);
//                }
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
