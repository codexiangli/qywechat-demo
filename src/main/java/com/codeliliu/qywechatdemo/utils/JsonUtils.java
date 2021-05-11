package com.codeliliu.qywechatdemo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author lixiang
 * @since 2021/5/10
 */
public class JsonUtils {
    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting() // 换行输出
                .create();
        return gson.toJson(obj);
    }
}
