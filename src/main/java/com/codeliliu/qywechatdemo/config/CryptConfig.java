package com.codeliliu.qywechatdemo.config;

import com.codeliliu.qywechatdemo.aes.WXBizMsgCrypt;
import com.codeliliu.qywechatdemo.factory.CryptFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixiang
 * @since 2021/5/10
 */
@Configuration
public class CryptConfig {

    @Bean
    public WXBizMsgCrypt providerMsgCrypt() {
        return CryptFactory.getProviderMsgCrypt();
    }

    @Bean
    public WXBizMsgCrypt suitCorpMsgCrypt() {
        return CryptFactory.getSuitCorpMsgCrypt();
    }

    @Bean
    public WXBizMsgCrypt suitMsgCrypt() {
        return CryptFactory.getSuitMsgCrypt();
    }

}
