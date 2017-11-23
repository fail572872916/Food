package com.food.lmln.food.model;


import com.food.lmln.food.bean.OrderTemp;

/**
 * Created by Administrator on 17/9/3.
 */

public interface OrderTempDao {

     void setOrderTemp(OrderTemp temp);
     String getOrderTemp();
     String delOrderTemp(OrderTemp temp);
     void updOrderTemp(OrderTemp temp);


}
