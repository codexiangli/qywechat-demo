package com.codeliliu.qywechatdemo.config;

import lombok.Data;

/**
 * @author lixiang
 * @since 2021/5/10
 */
@Data
public class WxCpTpProperties {

    /**
     * 服务商企业ID
     */
    private String corpId;

    private String corpSecret;

    /**
     * 第三方应用应用id
     */
    private String suiteId;

    private String secret;

    private String token;

    private String aesKey;

}
