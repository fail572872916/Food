package com.food.lmln.food.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.food.lmln.food.bean.FoodInfo;
import com.food.lmln.food.bean.FoodinfoSmall;
import com.food.lmln.food.bean.MenuButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.food.lmln.food.db.Constant.ASSETS_NAME;
import static com.food.lmln.food.db.Constant.BUFFER_SIZE;
import static com.food.lmln.food.db.Constant.DATABASE_NAME;
import static com.food.lmln.food.db.Constant.TABLE_NAME_DESCRIBE;
import static com.food.lmln.food.db.Constant.TABLE_NAME_FOODINFO;

/**
 * Created by Weili on 2017/6/14.
 */

public class DbManger {
    private  static  SqlHelper sqlHelper;
    private String DB_PATH;//路径
    private Context mContext;
    private  final String UID = "uid";
    private  final String NAME = "cuisine_name";
    private  final String MEAT_NAME = "meat_name";
    private  final String MID = "mid";
    private  final String DESCRIBE = "meat_describe";
    private  final String IMAGEURL = "meat_image";
    private  final String PRICE = "meat_price";


    public   static  SqlHelper  getInstance(Context context){
        if(sqlHelper==null){
            sqlHelper =  new SqlHelper(context);
        }
    return sqlHelper;
    }
    public DbManger(Context context) {
        this.mContext = context;
        DB_PATH = File.separator + "data"
                + Environment.getDataDirectory().getAbsolutePath() + File.separator
                + context.getPackageName() + File.separator + "databases" + File.separator;
    }
    /**
     * copy数据库
     */

    public void copyDBFile(){
        File dir = new File(DB_PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        if (!dbFile.exists()){
            InputStream is;
            OutputStream os;
            try {
                is = mContext.getResources().getAssets().open(ASSETS_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0){
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * 读取所有菜系
//     * @return
//     */
//    public List<MenuButton> getAlldDscribe(){
//        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DATABASE_NAME, null);
//        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_DESCRIBE, null);
//        List<MenuButton> result = new ArrayList<>();
//        MenuButton  menu;
//        while (cursor.moveToNext()){
//
//            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(UID)));
//            String name = cursor.getString(cursor.getColumnIndex(NAME));
//            menu = new MenuButton(id,name);
//            result.add(menu);
//        }
//        cursor.close();
//        db.close();
//
//        return result;
//    }

    public List<FoodinfoSmall> getAllFoodInfo(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DATABASE_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_FOODINFO, null);
        List<FoodinfoSmall> result = new ArrayList<>();
        FoodinfoSmall  foodInfo;
        while (cursor.moveToNext()){

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MID)));
            String name = cursor.getString(cursor.getColumnIndex(MEAT_NAME));
            String describe = cursor.getString(cursor.getColumnIndex(DESCRIBE));
            Double price = Double.valueOf(cursor.getString(cursor.getColumnIndex(PRICE)));
            String  iamge= cursor.getString(cursor.getColumnIndex(IMAGEURL));
            foodInfo = new FoodinfoSmall(id,name,describe,String.valueOf(price),iamge);
//            foodInfo = new FoodInfo(id,name);
            result.add(foodInfo);
        }
        cursor.close();
        db.close();

        return result;
    }

    /**
     *  更具当前页面查询对应集合的数据
     * @param db 数据库
     * @param tabName  表名
     * @param pageIndex  当前页码
     * @return  返回的list
     * @param pagesize   每页展示的条目
     * select * form  person ?,?    页码与所展示条目
     *  0,20  1
     *  20,40 2
     *  40,60 3
     */
    public static List<FoodinfoSmall> getListByPageIndex(SQLiteDatabase db, String tabName, int pageIndex , int pagesize) {
        Cursor cursor = null;

        int index = (pageIndex-1) * pagesize;
        if (db != null) {
            String  sql = "select * from "+tabName+" limit  ?,?";
            cursor= db.rawQuery(sql,new String[]{index+"",pagesize +""});
        }

        return cursorToLsit(cursor);
    }

    public static List<FoodinfoSmall> cursorToLsit(Cursor cursor) {
        List<FoodinfoSmall> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex(Constant.MID);
            int id = cursor.getInt((columnIndex));
            String name = cursor.getString(cursor.getColumnIndex(Constant.MEAT_NAME));
            String describe = cursor.getString(cursor.getColumnIndex(Constant.MEAT_DESCRIBE));
            int mid = cursor.getInt(cursor.getColumnIndex(Constant.MEAT_MID));
            double price = cursor.getDouble(cursor.getColumnIndex(Constant.MEAT_PRICE));
            String  image = cursor.getString(cursor.getColumnIndex(Constant.MEAT_IMAGE));
            FoodinfoSmall person = new FoodinfoSmall(id, name,describe,String.valueOf(price),image);
            list.add(person);
        }


        return list;
    }

    /**
     * @param db        数据库
     * @param tableName 表名
     * @return 返回的总数
     */
    public static int getCountPerson(SQLiteDatabase db, String tableName) {
        int count = 0;
        if (db != null) {
            Cursor cursor = db.rawQuery("select * from " + tableName, null);
            count = cursor.getCount();
        }

        return count;
    }
}
