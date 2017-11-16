package com.food.lmln.food.bean;

/**
 *  @author Weli
 *  @time 2017-11-16  11:51
 *  @describe
 */
public class TemporaryOrder {

    /**
     * date : fsad
     * time : fsdaff
     * desk_no : 2
     * consumptionID : fsda
     * foodName : fsad
     * foodPrice : 232
     * foodCount : 2
     */

    private String date;
    private String time;
    private String desk_no;
    private String consumptionId;
    private String foodName;
    private String foodPrice;
    private int foodCount;

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

    public String getDesk_no() {
        return desk_no;
    }

    public void setDesk_no(String desk_no) {
        this.desk_no = desk_no;
    }

    public String consumptionId() {
        return consumptionId;
    }

    public void consumptionId(String consumptionID) {
        this.consumptionId = consumptionID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public TemporaryOrder(String date, String time, String desk_no, String consumptionId, String foodName, String foodPrice, int foodCount) {
        this.date = date;
        this.time = time;
        this.desk_no = desk_no;
        this.consumptionId = consumptionId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodCount = foodCount;
    }
}
