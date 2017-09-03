package com.food.lmln.food.bean;

/**
 * Created by Administrator on 17/9/3.
 * 临时订单号
 */

public class OrderTemp {
    private String order_temp;

    public OrderTemp() {
    }

    public OrderTemp(String order_temp) {
        this.order_temp = order_temp;
    }

    public String getOrder_temp() {
        return order_temp;
    }

    public void setOrder_temp(String order_temp) {
        this.order_temp = order_temp;
    }

    @Override
    public String toString() {
        return "OrderTemp{" +
                "order_temp='" + order_temp + '\'' +
                '}';
    }
}
