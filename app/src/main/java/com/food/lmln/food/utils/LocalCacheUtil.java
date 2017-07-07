package com.food.lmln.food.utils;

/**
 * Created by Weili on 2017/6/15.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.MathContext;

/**
 * Created by：JW
 * 创建日期：2015/9/24 15:28.
 * 描述：本地缓存
 */
public class LocalCacheUtil {

    // 文件保存路径
//    public static final String CACHE_PATH = Environment.getExternalStorageDirectory()
//            .getAbsolutePath() + "/zhbj_bitmap_cache";
//    public static final String CACHE_PATH = Environment.getExternalStorageDirectory()
//            .getAbsolutePath() + "/zhbj_bitmap_cache";


    private String CACHE_PATH;


    public LocalCacheUtil() {
        // 判断当前时候有内存卡的存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 内存卡存在
            CACHE_PATH = "/mnt/sdcard/lm_food/" + "/lm_food_bitmap_cache";
        }else{
            CACHE_PATH = "/data/data/lm_food/" + "/lm_food_bitmap_cache";
        }

        System.out.println("cache_path:" + CACHE_PATH);
    }

    /**
     * 设置Bitmap数据到本地
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap) {

        try {
//            String fileName = MD5Utils.encode(url);
            String fileName = url;

            File file = new File(CACHE_PATH, fileName);

            File parentFile = file.getParentFile();

            if (!parentFile.exists()) {
                // 如果文件不存在，则创建文件夹
                parentFile.mkdirs();
            }

            // 将图片压缩到本地 第一个参数：
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 通过url获取Bitmap
     *
     * @param url
     */
    public Bitmap getBitmapFromLocal(String url) {

        try {
            File file = new File(CACHE_PATH, url);
//            File file = new File(CACHE_PATH, MD5Utils.encode(url));

            if (file.exists()) {
                // 如果文件存在
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}