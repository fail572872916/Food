package com.food.lmln.food.bean;

/**
 * 订单临时
 *  @author Weli
 *  @time 2017-11-16  11:51
 *  @describe
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
