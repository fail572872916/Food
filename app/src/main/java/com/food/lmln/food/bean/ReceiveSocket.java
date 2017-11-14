package com.food.lmln.food.bean;

import java.util.List;

/**
 * @author Administrator
 * @name CarControl
 * @class name：com.example.administrator.carcontrol.entity
 * @class describe
 * @time 2017-10-20 12:19
 * @change
 * @chang time
 * @class describe 接收Socket消息
 */

public class ReceiveSocket {
    /**
     * flaglist : ["如花"]
     * flag : 如花
     * socket_ip :
     * message : null
     * operationCode : onLine
     * heartBeat : null
     */

    private String flag;
    private String socket_ip;
    private String message;
    private String operationCode;
    private String heartBeat;
    private List<String> flaglist;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSocket_ip() {
        return socket_ip;
    }

    public void setSocket_ip(String socket_ip) {
        this.socket_ip = socket_ip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(String heartBeat) {
        this.heartBeat = heartBeat;
    }

    public List<String> getFlaglist() {
        return flaglist;
    }

    public void setFlaglist(List<String> flaglist) {
        this.flaglist = flaglist;
    }
}
