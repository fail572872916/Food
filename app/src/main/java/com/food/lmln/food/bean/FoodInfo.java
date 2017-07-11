package com.food.lmln.food.bean;

import java.util.List;

/**
 * Created by Weili on 2017/6/8.
 */
    public class FoodInfo {
        private int key;
        private List<FoodinfoSmall> list;
        public int getKey() {
            return key;
        }
        public void setKey(int key) {
            this.key = key;
        }
        public List<FoodinfoSmall> getList() {
            return list;
        }
        public void setList(List<FoodinfoSmall> list) {
            this.list = list;
        }
        public FoodInfo() {
            super();
        }
        @Override
        public String toString() {
            return "FoodInfo [key=" + key + ", list=" + list + "]";
        }



    }
