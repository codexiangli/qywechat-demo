package com.codeliliu.qywechatdemo.builer;

import com.codeliliu.qywechatdemo.tp.WxCpTpXmlOutMessage;
import me.chanjar.weixin.cp.bean.WxCpTpXmlPackage;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutImageMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;

/**
 * @author lixiang
 * @since 2021/5/10
 */
public class ImageBuilder extends AbstractBuilder {

    @Override
    public WxCpXmlOutMessage build(String content, WxCpTpXmlPackage wxMessage) {
        WxCpXmlOutMessage m = WxCpTpXmlOutMessage.IMAGE().mediaId(content)
                .fromUser(wxMessage.getToUserName())
                .toUser(getFromUser(wxMessage))
                .build();
        return m;
    }


    public WxCpXmlOutMessage build(String content,
                                   WxCpXmlMessage wxMessage) {
        WxCpXmlOutImageMessage m = WxCpXmlOutMessage.IMAGE().mediaId(content)
                .fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
                .build();
        return m;
    }
}
