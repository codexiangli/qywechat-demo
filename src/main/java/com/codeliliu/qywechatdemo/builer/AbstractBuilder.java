package com.codeliliu.qywechatdemo.builer;

import com.codeliliu.qywechatdemo.constant.WxCpTpInnerConstant;
import me.chanjar.weixin.cp.bean.WxCpTpXmlPackage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lixiang
 * @since 2021/5/10
 */
public abstract class AbstractBuilder {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 构建消息类型
     * @param content
     * @param wxMessage
     * @return
     */
    public abstract WxCpXmlOutMessage build(String content, WxCpTpXmlPackage wxMessage);

    protected String getFromUser(WxCpTpXmlPackage wxMessage){
        return wxMessage.getAllFieldsMap().get(WxCpTpInnerConstant.FROM_USER_NAME).toString();
    }
}
