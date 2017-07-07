package com.food.lmln.food.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.food.lmln.food.db.Constant.FILENAME;
import static com.food.lmln.food.utils.FileUtils.readList;
import static com.food.lmln.food.utils.FileUtils.rewrite;

/**
 * JAVA版本的自动生成有规则的订单号(或编号)
 * 生成的格式是: 200908010001 前面几位为当前的日期,后面五位为系统自增长类型的编号
 * 原理:
 *      1.获取当前日期格式化值;
 *      2.读取文件,上次编号的值+1最为当前此次编号的值
 *      (新的一天会重新从1开始编号)
 *
 *      存储自动编号值的文件如下(文件名: EveryDaySerialNumber.dat)
 */

public class OrderUtils {
public static String getOrderId() {
    SerialNumber serial = new FileEveryDaySerialNumber(4,FILENAME);
     String order =serial.getSerialNumber();
    return order;
}
}
 abstract  class SerialNumber {

    public synchronized String getSerialNumber() {
        return process();
    }
    protected abstract String process();
}


abstract class EveryDaySerialNumber extends SerialNumber {
    final	String   Order="D";

    protected final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    protected DecimalFormat df = null;

    public EveryDaySerialNumber(int width) {
        if(width < 1) {
            throw new IllegalArgumentException("Parameter length must be great than 1!");
        }
        char[] chs = new char[width];
        for(int i = 0; i < width; i++) {
            chs[i] = '0';
        }
        df = new DecimalFormat(new String(chs));
    }

    protected String process() {
        Date date = new Date();
        int n = getOrUpdateNumber(date, 1);
        return   Order+format(date) + format(n);
    }

    protected String format(Date date) {
        return sdf.format(date);
    }
    protected String format(int num) {
        return df.format(num);
    }

    /**
     * 获得序列号，同时更新持久化存储中的序列
     * @param current 当前的日期
     * @param start   初始化的序号
     * @return 所获得新的序列号
     */
    protected abstract int getOrUpdateNumber(Date current, int start);
}


class FileEveryDaySerialNumber extends EveryDaySerialNumber {
    /**
     * 持久化存储的文件
     */
    private File file = null;

    private String CACHE_PATH;

    /**
     * 存储的分隔符
     */
    private final static String FIELD_SEPARATOR = ",";


    public  FileEveryDaySerialNumber(int width, String filename) {

        super(width);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 内存卡存在
            CACHE_PATH = "/mnt/sdcard/lm_food/" + "/lm_food_bitmap_cache";
        }else{
            CACHE_PATH = "/data/data/lm_food/" + "/lm_food_bitmap_cache";
        }

        file = new File(CACHE_PATH, filename);
    }

    @Override
    protected int getOrUpdateNumber(Date current, int start) {
        String date = format(current);
        int num = start;
        if(file.exists()) {
            List<String> list = readList(file);
            String[] data = list.get(0).split(FIELD_SEPARATOR);
            if(date.equals(data[0])) {
                num = Integer.parseInt(data[1]);
            }
        }
        rewrite(file,  date + FIELD_SEPARATOR + (num + 1));
        return num;
    }




}








  
