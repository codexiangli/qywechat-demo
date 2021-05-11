package com.codeliliu.qywechatdemo.config;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author lixiang
 * @since 2021/5/10
 */
@Data
@ConfigurationProperties(prefix = "wx.cptp")
public class MultiWxCpTpProperties {

    private Map<String, WxCpTpProperties> properties = Maps.newHashMap();
}
