package com.food.lmln.food.bean;

import java.util.List;

/**
 * @author Administrator
 * @name Food
 * @class name：com.food.lmln.food.bean
 * @class describe
 * @time 2017-11-16 14:24
 * @change
 * @chang time
 * @class describe
 */
public class aa {

    /**
     * message : {"package":{"detail_food":[{"count":1,"price":"0.01","name":"微信测试"}],"people_num":0,"desk_num_str":"1号桌"},"result":true,"extra":{"desk_num_str":"1号桌"}}
     * socket_ip : 192.168.0.108
     * heartBeat :
     * flaglist : []
     * flag :
     * operationCode : P001
     */

    private String message;
    private String socket_ip;
    private String heartBeat;
    private String flag;
    private String operationCode;
    private List<?> flaglist;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSocket_ip() {
        return socket_ip;
    }
    public void setSocket_ip(String socket_ip) {
        this.socket_ip = socket_ip;
    }
    public String getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(String heartBeat) {
        this.heartBeat = heartBeat;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public List<?> getFlaglist() {
        return flaglist;
    }

    public void setFlaglist(List<?> flaglist) {
        this.flaglist = flaglist;
    }
}
