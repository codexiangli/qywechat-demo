package com.codeliliu.qywechatdemo.handler;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.WxCpTpXmlPackage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author lixiang
 * @since 2021/5/10
 */
@Slf4j
public abstract class AbstractHandler<T> implements Handler<T>{

    protected String getContent(WxCpTpXmlPackage wxMessage){
        Map<String, Object> allFieldsMap = wxMessage.getAllFieldsMap();
        return allFieldsMap.get("Content").toString();
    }


}
