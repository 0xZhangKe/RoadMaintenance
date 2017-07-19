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
     * 测试路径
     *
     */
//    public static String mainUrl = "http://192.168.199.125/road/";
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
     * 重新上传上传
     *
     */
    public static String readdDisease = mainUrl+"task/addDisease";
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
    /**
     * 获取进度
     *
     */
    public static String getWorkOrderLocation = mainUrl+"map/getWorkOrderLocation";
    /**
     * 修改密码
     *
     */
    public static String updateUserPass = mainUrl+"my/updateUserPass";
    /**
     * 发送验证码
     *
     */
    public static String sendSms = mainUrl+"login/sendSms";
    /**
     * 发送验证码,修改手机号
     *
     */
    public static String sendSms2 = mainUrl+"my/sendSms";
    /**
     * 修改手机号
     *
     */
    public static String updateUserTel = mainUrl+"my/updateUserTel";
    /**
     * 修改头像
     *
     */
    public static String uploadHeader = mainUrl+"my/uploadHeader";
}
