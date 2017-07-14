package com.jinjiang.roadmaintenance.utils;

/**
 * Created by wuwei on 2017/6/27.
 */

public class Uri {

    /**
     * 服务器主路径
     *
     */
    public static String mainUrl = "http://yun1.seagetech.com:28080/tjroad/";
    /**
     * 用户登录
     *
     */
    public static String loginUrl = mainUrl+"login/login";
    /**
     * 获取道路类型
     *
     */
    public static String getDicByTypeKey = mainUrl+"dectionary/getDicByTypeKey";
    /**
     * 获取病害类型
     *
     */
    public static String getDiseaseType = mainUrl+"disease/getDiseaseType";
    /**
     * 获取病害类型属性
     *
     */
    public static String getDiseaseAttr = mainUrl+"disease/getDiseaseAttr";
    /**
     * 上传
     *
     */
    public static String addDisease = mainUrl+"disease/addDisease";
    /**
     * 获取任务状态列表
     *
     */
    public static String getOrderWork = mainUrl+"task/getOrderWork";
    /**
     * 获取任务详情
     *
     */
    public static String getDiseaseInfos = mainUrl+"task/getDiseaseInfos";
    /**
     * 技术员确认信息
     *
     */
    public static String techAffirmDiseaseInfo = mainUrl+"task/techAffirmDiseaseInfo";
    /**
     * 审核信息
     *
     */
    public static String official = mainUrl+"task/official";
    /**
     * 三级业主审核信息
     *
     */
    public static String owner3Official = mainUrl+"task/owner3Official";
    /**
     * 获取施工方案
     *
     */
    public static String getMaintainFun = mainUrl+"disease/getMaintainFun";
    /**
     * 完工提交
     *
     */
    public static String saveConstructionInfo = mainUrl+"task/saveConstructionInfo";
    /**
     * 获取进度
     *
     */
    public static String getWorkOrderProgress = mainUrl+"progress/getWorkOrderProgress";
}
