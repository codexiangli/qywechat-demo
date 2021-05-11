package com.codeliliu.qywechatdemo.handler;

import com.codeliliu.qywechatdemo.utils.ApplicationContextHolder;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lixiang
 * @since 2021/5/11
 */
@Component
public class DefaultHandlerSelector implements HandlerSelector<WxCpXmlOutMessage> {

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private MsgHandler msgHandler;

    @Override
    public Handler<WxCpXmlOutMessage> select(String msgType) {
        switch (msgType) {
            case "event":
                return eventHandler;
            // todo 添加更多类型
            default:
                return msgHandler;
        }
    }
}
