package com.codeliliu.qywechatdemo.builer;

import me.chanjar.weixin.cp.bean.WxCpTpXmlPackage;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutNewsMessage;

/**
 * @author lixiang
 * @since 2021/5/10
 */
public class NewsBuilder extends AbstractBuilder {

    @Override
    public WxCpXmlOutMessage build(String content, WxCpTpXmlPackage wxMessage) {
        WxCpXmlOutNewsMessage.Item item1 = new WxCpXmlOutNewsMessage.Item();
        String picUrl = "https://img-blog.csdn.net/20180527145244476?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NDk5MDU5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70";
        item1.setUrl("https://blog.csdn.net/m0_37499059");
        item1.setPicUrl(picUrl);
        item1.setDescription("Is Really A Happy Day");
        item1.setTitle("这是图文消息卡片标题");

        WxCpXmlOutNewsMessage.Item item2 = new WxCpXmlOutNewsMessage.Item();
        item2.setUrl("http://www.baidu.com");
        item2.setPicUrl(picUrl);
        item2.setDescription("Is Really A Happy Day");
        item2.setTitle("Happy Day");
        WxCpXmlOutNewsMessage m = WxCpXmlOutMessage.NEWS()
                .addArticle(item2)
                .addArticle(item2)
                .fromUser(wxMessage.getToUserName())
                .toUser(getFromUser(wxMessage))
                .build();
        return m;
    }



    public WxCpXmlOutMessage build(String content,
                                   WxCpXmlMessage wxMessage) {

        WxCpXmlOutNewsMessage.Item item1 = new WxCpXmlOutNewsMessage.Item();
        String picUrl = "https://img-blog.csdn.net/20180527145244476?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NDk5MDU5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70";
        item1.setUrl("https://blog.csdn.net/m0_37499059");
        item1.setPicUrl(picUrl);
        item1.setDescription("Is Really A Happy Day");
        item1.setTitle("这是图文消息卡片标题");

        WxCpXmlOutNewsMessage.Item item2 = new WxCpXmlOutNewsMessage.Item();
        item2.setUrl("http://www.baidu.com");
        item2.setPicUrl(picUrl);
        item2.setDescription("Is Really A Happy Day");
        item2.setTitle("Happy Day");
        WxCpXmlOutNewsMessage m = WxCpXmlOutMessage.NEWS()
                .addArticle(item2)
                .addArticle(item2)
                .fromUser(wxMessage.getToUserName())
                .toUser(wxMessage.getFromUserName())
                .build();
        return m;
    }
}

