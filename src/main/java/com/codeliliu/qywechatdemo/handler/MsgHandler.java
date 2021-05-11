package com.codeliliu.qywechatdemo.handler;

import com.codeliliu.qywechatdemo.builer.ImageBuilder;
import com.codeliliu.qywechatdemo.builer.NewsBuilder;
import com.codeliliu.qywechatdemo.builer.TextBuilder;
import me.chanjar.weixin.cp.bean.WxCpTpXmlPackage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lixiang
 * @since 2021/5/10
 */
@Component
public class MsgHandler extends AbstractHandler<WxCpXmlOutMessage> {

    @Override
    public WxCpXmlOutMessage handle(WxCpTpXmlPackage wxMessage, Map<String, Object> context) {
        //@see XmlMsgType
        // 消息处理
        String content = getContent(wxMessage);
        WxCpXmlOutMessage xmlOutMessage = null;
        switch (content) {
            case "图片": {
                content = "1JwpbLSisNzaUb8dGiR6stmseQcRORha2Md9DeV829-1W_G56F6gjsO5ClJ7isccW";
                xmlOutMessage = new ImageBuilder().build(content, wxMessage);
                break;
            }
            case "图文": {
                xmlOutMessage = new NewsBuilder().build(content, wxMessage);
                break;
            }
            default: {
                xmlOutMessage =  new TextBuilder().build(content, wxMessage);
                break;
            }
        }
        return xmlOutMessage;
    }
}
