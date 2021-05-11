package com.codeliliu.qywechatdemo.handler;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.WxCpTpXmlPackage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lixiang
 * @since 2021/5/11
 */
@Component
@Slf4j
public class EventHandler extends AbstractHandler<WxCpXmlOutMessage> {

    @Override
    public WxCpXmlOutMessage handle(WxCpTpXmlPackage wxMessage, Map<String, Object> context) {
        // todo 根据不同的类型派发不同的 processor 处理
        log.info("触发event -> {}", wxMessage);
        return null;
    }
}
