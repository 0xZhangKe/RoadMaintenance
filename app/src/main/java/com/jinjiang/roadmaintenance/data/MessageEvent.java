package com.jinjiang.roadmaintenance.data;

/**
 * Created by wuwei on 2017/7/10.
 */

public class MessageEvent {
    public String message;
    public int code;

    public MessageEvent(int code,String message) {
        this.message = message;
        this.code = code;
    }
}
