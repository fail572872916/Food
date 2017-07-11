package com.food.lmln.food.bean;

/**
 * Created by Weili on 2017/6/5.
 */

public class OrderInfo {
    private int  id;

    private String  name;
    private  double  price;
    private int  count;
    private  boolean  flag;

    public OrderInfo(int id, String name, double price, int count, boolean flag) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", flag=" + flag +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
