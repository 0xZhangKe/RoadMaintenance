package com.jinjiang.roadmaintenance.model;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.jinjiang.roadmaintenance.data.BaseBean;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2016/12/21.
 */
public class NetWorkRequest {
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
    public void doGetRequest(final int flag, final boolean isShowDialog, String url, Map map) {
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
                    BaseBean result = JSON.parseObject(response,new TypeReference<BaseBean>(){});

                    if (result != null&&result.getCode()==0) {
                        uiDataListener.loadDataFinish(flag, result.getResult());
                    }else if (result!=null){
                        uiDataListener.onError(result.getCode()+"",result.getMessage());
                    }
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
     * Post请求
     *
     * @param map
     */
    public void doPostRequest(final int flag, final boolean isShowDialog, String url, Map map) {
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
                    BaseBean result = JSON.parseObject(response,new TypeReference<BaseBean>(){});

                    if (result != null&&result.getCode()==0) {
                        uiDataListener.loadDataFinish(flag, result.getResult());
                    }else if (result!=null){
                        uiDataListener.onError(result.getCode()+"",result.getMessage());
                    }
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
     * Post请求上传图片
     *
     * @param map
     */
    public void doPostUpload(final int flag, final boolean isShowDialog, String url, Map map, String name, List<File> files) {
        RequestParams params = new RequestParams((HttpCycleContext) context);//请求参数
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key;
            String value;
            key = it.next().toString();
            value = (String) map.get(key);
            params.addFormDataPart(key, value);
        }
        params.addFormDataPart(name, files, MediaType.parse("multipart/form-data"));//上传多个文件
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
                    BaseBean result = JSON.parseObject(response,new TypeReference<BaseBean>(){});

                    if (result != null&&result.getCode()==0) {
                        uiDataListener.loadDataFinish(flag, result.getResult());
                    }else if (result!=null){
                        uiDataListener.onError(result.getCode()+"",result.getMessage());
                    }
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
     * Post请求上传单图片
     *
     * @param map
     */
    public void doPostUploadone(final int flag, final boolean isShowDialog, String url, Map map, String name, File file) {
        RequestParams params = new RequestParams((HttpCycleContext) context);//请求参数
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key;
            String value;
            key = it.next().toString();
            value = (String) map.get(key);
            params.addFormDataPart(key, value);
        }
        params.addFormDataPart(name, file, MediaType.parse("multipart/form-data"));//上传多个文件
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
                    BaseBean result = JSON.parseObject(response,new TypeReference<BaseBean>(){});

                    if (result != null&&result.getCode()==0) {
                        uiDataListener.loadDataFinish(flag, result.getResult());
                    }else if (result!=null){
                        uiDataListener.onError(result.getCode()+"",result.getMessage());
                    }
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
     * Post请求上传多组图片
     *
     * @param map
     */
    public void doPostUpload(final int flag, final boolean isShowDialog, String url, Map map, String name, List<File> files, String name2, List<File> files2) {
        RequestParams params = new RequestParams((HttpCycleContext) context);//请求参数
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key;
            String value;
            key = it.next().toString();
            value = (String) map.get(key);
            params.addFormDataPart(key, value);
        }
        params.addFormDataPart(name, files, MediaType.parse("multipart/form-data"));//上传多个文件
        params.addFormDataPart(name2, files2, MediaType.parse("multipart/form-data"));//上传多个文件
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
                    BaseBean result = JSON.parseObject(response,new TypeReference<BaseBean>(){});

                    if (result != null&&result.getCode()==0) {
                        uiDataListener.loadDataFinish(flag, result.getResult());
                    }else if (result!=null){
                        uiDataListener.onError(result.getCode()+"",result.getMessage());
                    }
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
