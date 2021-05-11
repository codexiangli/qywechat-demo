package com.codeliliu.qywechatdemo.builer;

import me.chanjar.weixin.cp.bean.WxCpTpXmlPackage;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutTextMessage;

/**
 * @author lixiang
 * @since 2021/5/10
 */
public class TextBuilder extends AbstractBuilder {

    @Override
    public WxCpXmlOutMessage build(String content, WxCpTpXmlPackage wxMessage) {
        WxCpXmlOutTextMessage m = WxCpXmlOutMessage.TEXT().content(content)
                .fromUser(wxMessage.getToUserName())
                .toUser(getFromUser(wxMessage))
                .build();
        return m;
    }

    public WxCpXmlOutMessage build(String content,
                                   WxCpXmlMessage wxMessage) {
        WxCpXmlOutTextMessage m = WxCpXmlOutMessage.TEXT().content(content)
                .fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
                .build();
        return m;
    }
}
