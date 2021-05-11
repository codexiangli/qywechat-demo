package com.codeliliu.qywechatdemo.handler;

/**
 * @author lixiang
 * @since 2021/5/11
 */
public interface HandlerSelector<T> {

    /**
     * 根据消息类型消息处理器
     * @param msgType
     * @return
     */
    Handler<T> select(String msgType);
}
