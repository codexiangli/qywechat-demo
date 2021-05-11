package com.codeliliu.qywechatdemo.tp;

import com.codeliliu.qywechatdemo.utils.ApplicationContextHolder;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.cp.config.impl.WxCpTpDefaultConfigImpl;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author lixiang
 * @since 2021/5/10
 */
public class WxCpTpRedisConfigImpl extends WxCpTpDefaultConfigImpl {


    private StringRedisTemplate stringRedisTemplate;

    /**
     * suite_ticket前缀 与suite_secret配套使用，用于获取suite_access_token 10min
     */
    private static final String SUITE_TICKET_PREFIX = "wx:cptp:suite_ticket:";

    /**
     * 第三方应用凭证前缀 2h （key按应用区分）
     */
    private static final String SUITE_ACCESS_TOKEN_KEY_PREFIX = "wx:cptp:suite_access_token:";


    public WxCpTpRedisConfigImpl() {
        stringRedisTemplate = ApplicationContextHolder.getBean("stringRedisTemplate");
    }

    /**-------------------------- suite ticket -------------------------**/
    @Override
    public String getSuiteTicket() {
        if (isSuiteTicketExpired()){
            String result = "{\"errcode\":40085, \"errmsg\":\"invaild suite ticket\"}";
            System.err.println(result);
            return null;
        }
        return stringRedisTemplate.opsForValue().get(SUITE_TICKET_PREFIX + getSuiteId() + ":");
    }

    @Override
    public boolean isSuiteTicketExpired() {
       return !(stringRedisTemplate.getExpire(SUITE_TICKET_PREFIX + getSuiteId() + ":") > 0L);
    }

    @Override
    public synchronized void updateSuiteTicket(String suiteTicket, int expiresInSeconds) {
        stringRedisTemplate.opsForValue().set(SUITE_TICKET_PREFIX + getSuiteId() + ":", suiteTicket, expiresInSeconds - 200 , TimeUnit.SECONDS);
        System.out.println("redis缓存更新成功：" + suiteTicket);
    }

    @Override
    public void expireSuiteTicket() {
        stringRedisTemplate.expire(SUITE_TICKET_PREFIX + getSuiteId() + ":", 0, TimeUnit.SECONDS);
    }

    /**-------------------------- suite access token -------------------------**/
    @Override
    public String getSuiteAccessToken() {
        if (isSuiteAccessTokenExpired()){
            return null;
        }
        return stringRedisTemplate.opsForValue().get(SUITE_ACCESS_TOKEN_KEY_PREFIX + getSuiteId() + ":");
    }

    @Override
    public boolean isSuiteAccessTokenExpired() {
        return !(stringRedisTemplate.getExpire(SUITE_ACCESS_TOKEN_KEY_PREFIX + getSuiteId() + ":") > 0L);
    }

    @Override
    public synchronized void updateSuiteAccessToken(WxAccessToken suiteAccessToken) {
        this.updateSuiteAccessToken(suiteAccessToken.getAccessToken(), suiteAccessToken.getExpiresIn());
    }

    @Override
    public synchronized void updateSuiteAccessToken(String suiteAccessToken, int expiresInSeconds) {
        stringRedisTemplate.opsForValue().set(SUITE_ACCESS_TOKEN_KEY_PREFIX + getSuiteId() + ":", suiteAccessToken, expiresInSeconds, TimeUnit.SECONDS);
        System.out.println("redis缓存更新成功：" + suiteAccessToken);
    }

    @Override
    public void expireSuiteAccessToken() {
        stringRedisTemplate.expire(SUITE_ACCESS_TOKEN_KEY_PREFIX + getSuiteId() + ":", 0, TimeUnit.SECONDS);
    }

}
