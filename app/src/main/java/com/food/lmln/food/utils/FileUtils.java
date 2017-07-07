package com.food.lmln.food.utils;

/**
 * Created by Weili on 2017/6/9.
 */


        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.FileReader;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Bitmap.CompressFormat;
        import android.graphics.BitmapFactory;
        import android.os.Environment;
        import android.util.Log;

        import com.food.lmln.food.BuildConfig;
        import com.food.lmln.food.db.Constant;

        import static com.food.lmln.food.db.Constant.FILENAME;
        import static com.food.lmln.food.utils.OrderUtils.getOrderId;


public class FileUtils {
    /**
     * sd卡的根目录
     */
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
    /**
     * 手机的缓存根目录
     */
    private static String mDataRootPath = null;
    /**
     * 保存Image的目录名
     */
    private final static String FOLDER_NAME = "/AndroidImage";


    public FileUtils(Context context){
        mDataRootPath = context.getCacheDir().getPath();
    }


    /**
     * 获取储存Image的目录
     * @return
     */
    private String getStorageDirectory(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
    }

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    public void savaBitmap(String fileName, Bitmap bitmap) throws IOException{
        if(bitmap == null){
            return;
        }
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if(!folderFile.exists()){
            folderFile.mkdir();
        }
        File file = new File(path + File.separator + fileName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }

    /**
     * 从手机或者sd卡获取Bitmap
     * @param fileName
     * @return
     */
    public Bitmap getBitmap(String fileName){
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
    }

    /**
     * 判断文件是否存在
     * @param fileName
     * @return
     */
    public boolean isFileExists(String fileName){
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }

    /**
     * 获取文件的大小
     * @param fileName
     * @return
     */
    public long getFileSize(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }


    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    public void deleteFile() {
        File dirFile = new File(getStorageDirectory());
        if(! dirFile.exists()){
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }

        dirFile.delete();
    }


        public static void rewrite(File file, String data) {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(file));
                bw.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(bw != null) {
                    try {
                        bw.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public static List<String> readList(File file) {
            BufferedReader br = null;
            List<String> data = new ArrayList<String>();
            try {
                br = new BufferedReader(new FileReader(file));
                for(String str = null; (str = br.readLine()) != null; ) {
                    data.add(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(br != null) {
                    try {
                        br.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data;
        }

    private final static String FIELD_SEPARATOR = ",";
    private static  String CACHE_PATH;
        public static void rewriteOrdera(String order){
            int ordrNo=0;
            LocalCacheUtil();
            if(order.length()>10) {
                 ordrNo = Integer.parseInt(order.substring(9, 13));
            }
            else {
                ordrNo=1;
            }
            int num=1;
            Date current = new Date();
            String date = format(current);
             File      file = new File(CACHE_PATH, Constant.FILENAME);
            if(!file.exists()) {
                getOrderId();

            }
                rewrite(file,  date + FIELD_SEPARATOR +(ordrNo+num));

            Log.d("FileUtils", "file:" + file+date + FIELD_SEPARATOR + ordrNo);


        }

    protected final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    protected static String format(Date date) {
        return sdf.format(date);
    }
     public static void  LocalCacheUtil() {
        // 判断当前时候有内存卡的存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 内存卡存在
            CACHE_PATH = "/mnt/sdcard/lm_food/" + "/lm_food_bitmap_cache";
        }else{
            CACHE_PATH = "/data/data/lm_food/" + "/lm_food_bitmap_cache";
        }

        System.out.println("cache_path:" + CACHE_PATH);
    }

}
