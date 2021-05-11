package com.codeliliu.qywechatdemo.config;

import com.codeliliu.qywechatdemo.tp.WxCpTpRedisConfigImpl;
import com.google.common.collect.Maps;
import me.chanjar.weixin.cp.api.WxCpTpService;
import me.chanjar.weixin.cp.api.impl.WxCpTpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpTpDefaultConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

/**
 * @author lixiang
 * @since 2021/5/10
 */
@Configuration
@EnableConfigurationProperties(MultiWxCpTpProperties.class)
public class WxCpTpConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(WxCpTpConfiguration.class);
    public static String SUIT_ID = "ww3838b5bb841a835e";

    /**
     * suitId -> WxCpTpService
     */
    private static Map<String, WxCpTpService> cpTpServices;

    @Autowired
    private MultiWxCpTpProperties multiVirtualHostProperties;

    public static WxCpTpService getCpTpService(String suitId) {
        return cpTpServices.get(suitId);
    }

    @Bean
    public Map<String, WxCpTpService> cpTpServices(@Autowired(required = false) StringRedisTemplate redisTemplate,
                                                   MultiWxCpTpProperties multiVirtualHostProperties) {

        Map<String, WxCpTpService> cpTpServiceMap= Maps.newHashMap();
        multiVirtualHostProperties.getProperties().forEach((suitName, prop) -> {
            WxCpTpService tpService = new WxCpTpServiceImpl();
            WxCpTpDefaultConfigImpl configStorage = new WxCpTpDefaultConfigImpl();
            // 加入redis缓存
            if (redisTemplate != null) {
                configStorage = new WxCpTpRedisConfigImpl();
            }
            configStorage.setSuiteId(prop.getSuiteId());
            configStorage.setAesKey(prop.getAesKey());
            configStorage.setToken(prop.getToken());
            configStorage.setSuiteSecret(prop.getSecret());
            configStorage.setCorpId(prop.getCorpId());

            tpService.setWxCpTpConfigStorage(configStorage);
            cpTpServiceMap.put(prop.getSuiteId(), tpService);
        });
        cpTpServices = cpTpServiceMap;
        return cpTpServiceMap;
    }

    /**
     * redis是否可用
     * @return
     *//*
    private boolean redisIsOk(){
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.ping();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }*/
}
