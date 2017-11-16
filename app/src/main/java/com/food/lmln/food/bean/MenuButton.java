package com.food.lmln.food.bean;

/**
 *  @author Weli
 *  @time 2017-11-16  11:57
 *  @describe 
 */
public class MenuButton {
    private int id;
    private String name;
    private String describe;
    private String price;
    private String version;

    @Override
    public String toString() {
        return "MenuButton{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", price='" + price + '\'' +
                ", version='" + version + '\'' +
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public MenuButton(int id, String name, String describe, String price, String version) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.price = price;
        this.version = version;
    }
}
