package com.food.lmln.food.bean;

import java.util.List;

/**
 * Created by Weili on 2017/6/8.
 */

public class FoodInfo {



    private  int id;  //id
    private  String name ;  //名字
    private  String describe ;  //详情
    private  Double price ;  //价格
    private  String iamge ;  //图片

    public String getIamge() {
        return iamge;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "FoodInfo{" +
                "iamge='" + iamge + '\'' +
                ", describe='" + describe + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", price=" + price +
                '}';
    }

    public FoodInfo(int id, String name, String describe, Double price, String iamge) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.price = price;
        this.iamge = iamge;
    }
}
