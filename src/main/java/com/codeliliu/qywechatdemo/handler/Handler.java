package com.codeliliu.qywechatdemo.handler;

import me.chanjar.weixin.cp.bean.WxCpTpXmlPackage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;

import java.util.Map;

/**
 * @author lixiang
 * @since 2021/5/11
 */
public interface Handler<T> {

    /**
     * 消息处理器
     * @param wxMessage
     * @param context
     * @return
     */
    T handle(WxCpTpXmlPackage wxMessage, Map<String, Object> context);
}
