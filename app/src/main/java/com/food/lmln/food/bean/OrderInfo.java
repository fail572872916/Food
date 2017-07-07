package com.food.lmln.food.bean;

/**
 * Created by Weili on 2017/6/5.
 */

public class OrderInfo {
    private int  id;

    private String  name;
    private  double  price;
    private int  count;

    public OrderInfo(int id, String name, double price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
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

    @Override
    public String toString() {
        return "OrderInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }

    //判断是否是同一个箱子
    public boolean equals(Object o){
        OrderInfo s = (OrderInfo)o;
        return s.getName().equals(name)&& s.getName().equals(name)?true:false;
    }
}
