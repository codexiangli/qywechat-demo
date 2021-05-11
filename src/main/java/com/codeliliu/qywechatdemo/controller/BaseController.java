package com.codeliliu.qywechatdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.codeliliu.qywechatdemo.utils.HttpUtils;
import me.chanjar.weixin.cp.api.WxCpTpService;


/**
 * @author lixiang
 * @since 2021/5/11
 */
public class BaseController {

    private static String getuserinfo3rdUrl = "https://qyapi.weixin.qq.com/cgi-bin/service/getuserinfo3rd?suite_access_token=%s&code=%s";
    private static String getuserdetail3rdUrl = "https://qyapi.weixin.qq.com/cgi-bin/service/getuserdetail3rd?suite_access_token=%s";

    /**
     * 获取用户信息
     * getSuiteAccessToken 缓存期2小时
     * @throws Exception
     */
    public static JSONObject getUserInfo3rd(WxCpTpService tpService, String code) throws Exception{
        JSONObject result = HttpUtils.sendGet(String.format(getuserinfo3rdUrl, tpService.getSuiteAccessToken(), code));
        System.out.println(result.toString());
        System.out.println("user_ticket：" + result.get("user_ticket"));
        return result;
    }

    /**
     * 获取访问用户敏感信息
     * @throws Exception
     */
    public static JSONObject getUserDetail3rd(WxCpTpService tpService, String user_ticket) throws Exception{
        JSONObject object = new JSONObject();
        object.put("user_ticket", user_ticket);
        JSONObject result = HttpUtils.sendPost(String.format(getuserdetail3rdUrl, tpService.getSuiteAccessToken()), object.toJSONString());
        System.out.println("用户详细信息："+result.toJSONString());
        return result;
    }
}
