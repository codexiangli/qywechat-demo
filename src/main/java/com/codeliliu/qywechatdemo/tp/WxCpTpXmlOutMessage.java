package com.codeliliu.qywechatdemo.tp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.config.WxCpTpConfigStorage;
import me.chanjar.weixin.cp.util.crypto.WxCpTpCryptUtil;

/**
 * @author lixiang
 * @since 2021/5/10
 */
@XStreamAlias("xml")
@Data
@EqualsAndHashCode(callSuper = false)
public class WxCpTpXmlOutMessage extends WxCpXmlOutMessage {

    public String toEncryptedXml(WxCpTpConfigStorage tpConfigStorage) {
        String plainXml = toXml();
        WxCpTpCryptUtil pc = new WxCpTpCryptUtil(tpConfigStorage);
        return pc.encrypt(plainXml);
    }


}
