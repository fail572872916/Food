package com.food.lmln.food.bean;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/31.
 */

public class DeskTemp {
    private String date;
    private String  time;
    private String deskNO;
    private String  consumptionId;
    private String  foodName;
    private double foodPrice;
    private double foodCount;

    @Override
    public String toString() {
        return "DeskTemp{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", deskNO='" + deskNO + '\'' +
                ", consumptionId='" + consumptionId + '\'' +
                ", foodName='" + foodName + '\'' +
                ", foodPrice=" + foodPrice +
                ", foodCount=" + foodCount +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeskNO() {
        return deskNO;
    }

    public void setDeskNO(String deskNO) {
        this.deskNO = deskNO;
    }

    public String getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(String consumptionId) {
        this.consumptionId = consumptionId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public double getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(double foodCount) {
        this.foodCount = foodCount;
    }

    public DeskTemp(String date, String time, String deskNO, String consumptionId, String foodName, double foodPrice, double foodCount) {
        this.date = date;
        this.time = time;
        this.deskNO = deskNO;
        this.consumptionId = consumptionId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodCount = foodCount;
    }
}
