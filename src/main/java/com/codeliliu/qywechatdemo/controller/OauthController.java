package com.codeliliu.qywechatdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.codeliliu.qywechatdemo.Constant;
import com.codeliliu.qywechatdemo.config.WxCpTpConfiguration;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpTpService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lixiang
 * @since 2021/5/11
 */
@Slf4j
@RestController
@RequestMapping("/wx/oauth")
public class OauthController extends BaseController {

    private WxCpTpService tpService;
    private String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_privateinfo&state=%s#wechat_redirect";
    private String loginUrl = "https://rs.epharmacloud.com/wx/oauth/login";
    /**
     * 跳转授权链接
     * @param state
     */
    @GetMapping("/auth")
    public void jump(String state,
                     HttpServletResponse response) throws IOException {
        oauthUrl = String.format(oauthUrl, Constant.CRM_TEST_SUITE_ID, loginUrl, state);
        log.info("跳转url:" + oauthUrl);
        response.sendRedirect(oauthUrl);
    }

    /**
     * 授权链接  通过code换取用户信息
     */
    @GetMapping("/login")
    public void login(String code,
                      String state,
                      HttpServletRequest request,
                      HttpServletResponse response) {
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("code is empty");
        }
        try {
            tpService = WxCpTpConfiguration.getCpTpService(Constant.CRM_TEST_SUITE_ID);
            JSONObject userInfo3rd = getUserInfo3rd(tpService, code);
            if (userInfo3rd == null){
                throw new Exception("用户信息获取失败");
            }
            JSONObject userInfoDetail = getUserDetail3rd(tpService, userInfo3rd.get("user_ticket").toString());
            request.getSession().setAttribute("token", userInfoDetail);
            response.sendRedirect(state);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
