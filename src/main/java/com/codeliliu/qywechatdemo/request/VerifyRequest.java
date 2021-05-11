package com.codeliliu.qywechatdemo.request;

import lombok.Data;

/**
 * @author lixiang
 * @since 2021/5/10
 */
@Data
public class VerifyRequest {
    private String msg_signature;
    private String timestamp;
    private String nonce;
    private String echostr;
}
