package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;

/**
 * 首页病害类型选择grid的item
 * Created by wuwei on 2017/6/19.
 */

public class EventTypeGrid implements Serializable {
    private int img;
    private String text;

    public EventTypeGrid(int img, String text) {
        this.img = img;
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
