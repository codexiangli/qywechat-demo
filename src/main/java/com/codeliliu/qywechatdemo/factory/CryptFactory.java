package com.codeliliu.qywechatdemo.factory;

import com.codeliliu.qywechatdemo.Constant;
import com.codeliliu.qywechatdemo.aes.AesException;
import com.codeliliu.qywechatdemo.aes.WXBizMsgCrypt;

/**
 * @author lixiang
 * @since 2021/5/10
 */
public class CryptFactory {

    // todo 可配置化

    public static WXBizMsgCrypt getProviderMsgCrypt() {
        WXBizMsgCrypt msgCrypt = null;
        try {
            msgCrypt = new WXBizMsgCrypt(Constant.TOKEN, Constant.ENCODING_AES_KEY, Constant.CORP_ID);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return msgCrypt;
    }

    public static WXBizMsgCrypt getSuitCorpMsgCrypt() {
        WXBizMsgCrypt msgCrypt = null;
        try {
            msgCrypt = new WXBizMsgCrypt(Constant.CRM_TEST_TOKEN, Constant.CRM_TEST_ENCODING_AES_KEY, Constant.CORP_ID);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return msgCrypt;
    }

    public static WXBizMsgCrypt getSuitMsgCrypt() {
        WXBizMsgCrypt msgCrypt = null;
        try {
            msgCrypt = new WXBizMsgCrypt(
                    Constant.CRM_TEST_TOKEN, Constant.CRM_TEST_ENCODING_AES_KEY, Constant.CRM_TEST_SUITE_ID);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return msgCrypt;
    }
}
