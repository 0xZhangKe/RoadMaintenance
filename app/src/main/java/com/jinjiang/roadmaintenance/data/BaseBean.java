package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;

/**
 * Created by wuwei on 2017/6/27.
 */

public class BaseBean<T> implements Serializable {
    private T data;
    private boolean success;
    private String msg;
    private int code;


}
