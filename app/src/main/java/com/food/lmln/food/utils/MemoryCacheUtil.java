package com.food.lmln.food.utils;

/**
 * Created by Weili on 2017/6/15.
 */


import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by：JW
 * 创建日期：2015/9/24 19:16.
 * 描述：内存缓存
 */
public class MemoryCacheUtil {

    private LruCache<String,Bitmap> mLruCache ;

    public MemoryCacheUtil(){

        // maxMemory 是允许的最大值 ，超过这个最大值，则会回收
        long maxMemory = Runtime.getRuntime().maxMemory(); // 获取最大的可用内存 一般使用可用内存的1 / 8

        mLruCache = new LruCache<String,Bitmap>((int) maxMemory){
            /**
             * 计算每张图片的大小
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {

                int size = value.getRowBytes() * value.getHeight();

                return size;
            }
        };

    }

    /**
     * 通过url从内存中获取图片
     * @param url
     */
    public Bitmap getBitmapFromMemory(String url){
        Bitmap bitmap = mLruCache.get(url);
        return bitmap;
    }

    /**
     * 设置Bitmap到内存
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url,Bitmap bitmap){
        mLruCache.put(url,bitmap); // 设置图片
    }

}