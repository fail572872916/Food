package com.food.lmln.food.bean;

/**
 * Created by Weili on 2017/6/2.
 */

public class MenuButton {
        private int id; //相应id
      private  String name; //功能名字

    public MenuButton(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "MenuButton{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
