package com.food.lmln.food.bean;

/**
 * Created by Weili on 2017/7/20.
 */

public class DeskInfo {
    private int id;
    private  String  local_ip;
    private  String  local_desk;


    public DeskInfo(String local_ip, String local_desk) {

        this.local_ip = local_ip;
        this.local_desk = local_desk;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocal_ip() {
        return local_ip;
    }

    public void setLocal_ip(String local_ip) {
        this.local_ip = local_ip;
    }

    public String getLocal_desk() {
        return local_desk;
    }

    public void setLocal_desk(String local_desk) {
        this.local_desk = local_desk;
    }

    @Override
    public String toString() {
        return "DeskInfo{" +
                "id=" + id +
                ", local_ip='" + local_ip + '\'' +
                ", local_desk='" + local_desk + '\'' +
                '}';
    }
}
