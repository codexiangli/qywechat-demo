package com.codeliliu.qywechatdemo.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lixiang
 * @since 2021/5/10
 */
public class QyWechatCache {

    private static ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public static void put(String key, String value) {
        cache.put(key, value);
    }

    public static String get(String key) {
        return cache.get(key);
    }
}
