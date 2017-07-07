package com.food.lmln.food.db;

import android.os.Bundle;
import android.os.Message;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.food.lmln.food.db.Constant.CONSUMPTIONID;
import static com.food.lmln.food.db.Constant.DESKTEMP_TIME;
import static com.food.lmln.food.db.Constant.DESK_TEMP;
import static com.food.lmln.food.db.Constant.send_msg_code4;
import static com.food.lmln.food.utils.FileUtils.rewriteOrdera;

/**
 * Created by Weili on 2017/7/7.
 */

public class MYsqlHelper {

    public static Connection getConnection()
    {
        Connection con=null;
        try
        {
//            Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver");
//            Class.forName("org.gjt.mm.mysql.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://103.45.11.232/lm_food",  "root", "root");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return con;
    }



    //知道学生的学号得到他的密码
    public static String selectPwd()
    {
        String result="";
        try
        {
            Connection con=getConnection();
            Statement st=con.createStatement();
            String sql = "select "+CONSUMPTIONID+" from "+DESK_TEMP+" order by "+DESKTEMP_TIME+" desc limit 0,1;";
            ResultSet rs=st.executeQuery(sql);
            if(rs.next())
            {
                result=rs.getString("CONSUMPTIONID");

            }
            rs.close();
            st.close();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    public static int selectClear()  {
        int num = 0;
      
        try {
            Connection con=getConnection();

            Statement st=con.createStatement();
            String sql = "select "+CONSUMPTIONID+" from "+DESK_TEMP+";";
            ResultSet rs=st.executeQuery(sql);
            if(rs.next()){
                num=1;
            }else{
                num=2;
            }
            rs.close();
            st.close();
            con.close();
        }   catch(Exception e)
        {
            e.printStackTrace();
        }

        return num;
    }


}
