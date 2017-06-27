package com.jinjiang.roadmaintenance.model;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

/**
 * Created by Administrator on 2016/12/21.
 */
public class NetWorkRequest<T> {
    private Context context;
    private UIDataListener uiDataListener;
    private String currentUrl = "";

    public NetWorkRequest(Context context, UIDataListener uiDataListener) {
        this.context = context;
        this.uiDataListener = uiDataListener;
    }

    /**
     * Get请求
     *
     * @param map
     */
    public void doGetRequest(final int flag, final boolean isShowDialog, String url, Map map, T t) {
        RequestParams params = new RequestParams((HttpCycleContext) context);//请求参数
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key;
            String value;
            key = it.next().toString();
            value = (String) map.get(key);
            params.addFormDataPart(key, value);
        }
        currentUrl = url;
        LogUtils.d(currentUrl + params);
        HttpRequest.get(url, params, new BaseHttpRequestCallback<String>() {

            //请求网络前
            @Override
            public void onStart() {
                if (isShowDialog)
                uiDataListener.showDialog();
            }

            @Override
            protected void onSuccess(String response) {
                if (TextUtils.isEmpty(response)) {
                    return;
                } else {
                    LogUtils.d(response);
//                    JsonArray data = new JsonParser().parse(response).getAsJsonArray();
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<T>() {
//                    }.getType();
//                    T result = gson.fromJson(data, type);
//                    if (result != null) {
//                        uiDataListener.loadDataFinish(flag, result);
//                    }
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                LogUtils.d("onFailure" + msg);
                uiDataListener.onError(errorCode+"", msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                uiDataListener.dismissDialog();
            }
        });
    }
    /**
     * Get请求
     *
     * @param map
     */
    public void doPostRequest(final int flag, final boolean isShowDialog, String url, Map map) {
        RequestParams params = new RequestParams((HttpCycleContext) context);//请求参数
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        params.applicationJson(JSONObject.parseObject(jsonStr));
        currentUrl = url;
        LogUtils.d(currentUrl + params);
        HttpRequest.post(url, params, new BaseHttpRequestCallback<String>() {

            //请求网络前
            @Override
            public void onStart() {
                if (isShowDialog)
                uiDataListener.showDialog();
            }

            @Override
            protected void onSuccess(String response) {
                if (TextUtils.isEmpty(response)) {
                    return;
                } else {
                    LogUtils.d(response);
//                    JsonArray data = new JsonParser().parse(response).getAsJsonArray();
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<T>() {
//                    }.getType();
//                    T result = gson.fromJson(data, type);
//                    if (result != null) {
//                        uiDataListener.loadDataFinish(flag, result);
//                    }
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                LogUtils.d("onFailure" + msg);
                uiDataListener.onError(errorCode+"", msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                uiDataListener.dismissDialog();
            }
        });
    }

    /**
     * 取消请求
     */
    public void CancelPost() {
        HttpRequest.cancel(currentUrl);
    }
}
