package com.food.lmln.food.model;


import com.food.lmln.food.bean.OrderTemp;

/**
 * Created by Administrator on 17/9/3.
 */

public interface OrderTempDao {

     void setOrderemp(OrderTemp temp);
     String getOrderemp();
     String delOrderemp(OrderTemp temp);
     void updOrderemp(OrderTemp temp);


}
