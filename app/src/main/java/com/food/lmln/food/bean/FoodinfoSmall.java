package com.food.lmln.food.bean;

/**
 * Created by Administrator on 2017/7/9.
 */

public class FoodinfoSmall {

    private  int id;  //id
    private  String name ;  //名字
    private  String describe ;  //详情
    private  String price ;  //价格
    private  String iamge ;  //图片


    public FoodinfoSmall(int id, String name, String describe, String price,
                         String iamge) {
        super();
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.price = price;
        this.iamge = iamge;
    }



    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", describe="
                + describe + ", price=" + price + ", iamge=" + iamge + "";
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
    public String getDescribe() {
        return describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getIamge() {
        return iamge;
    }
    public void setIamge(String iamge) {
        this.iamge = iamge;
    }

}
