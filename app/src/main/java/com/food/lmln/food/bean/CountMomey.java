package com.food.lmln.food.bean;

/**
 * Created by Weili on 2017/6/19.
 */

public class CountMomey {

    private int id;
    private double money;

    public CountMomey(int id, double money) {
        this.id = id;
        this.money = money;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
