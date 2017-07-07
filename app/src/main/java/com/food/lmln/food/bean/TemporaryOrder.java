package com.food.lmln.food.bean;

import java.util.List;

/**
 * Created by Weili on 2017/7/6.
 */

public class TemporaryOrder {

    private List<RECORDSBean> RECORDS;

    public List<RECORDSBean> getRECORDS() {
        return RECORDS;
    }

    public void setRECORDS(List<RECORDSBean> RECORDS) {
        this.RECORDS = RECORDS;
    }

    public static class RECORDSBean {
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
        private int desk_no;
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

        public int getDesk_no() {
            return desk_no;
        }

        public void setDesk_no(int desk_no) {
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
    }
}
