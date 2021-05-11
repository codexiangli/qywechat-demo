package com.codeliliu.qywechatdemo.controller;

import com.codeliliu.qywechatdemo.cache.QyWechatCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lixiang
 * @since 2021/5/10
 */
@RestController
@RequestMapping("/access")
@Slf4j
public class DataAccessController {

    @GetMapping("/{key}")
    public String getCacheData(@PathVariable String key) {
        return QyWechatCache.get(key);
    }
}
