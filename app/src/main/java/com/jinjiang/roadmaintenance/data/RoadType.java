package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/6/29.
 */

public class RoadType implements Serializable {

    /**
     * text : 沥青路面
     * value : 1
     */

    private String text;
    private int value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
