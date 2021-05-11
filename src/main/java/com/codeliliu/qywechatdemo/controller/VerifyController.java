package com.codeliliu.qywechatdemo.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.codeliliu.qywechatdemo.Constant;
import com.codeliliu.qywechatdemo.InfoTypeEnum;
import com.codeliliu.qywechatdemo.aes.WXBizMsgCrypt;
import com.codeliliu.qywechatdemo.config.WxCpTpConfiguration;
import com.codeliliu.qywechatdemo.handler.HandlerSelector;
import com.codeliliu.qywechatdemo.request.VerifyRequest;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpTpService;
import me.chanjar.weixin.cp.bean.WxCpTpCorp;
import me.chanjar.weixin.cp.bean.WxCpTpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpTpXmlPackage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.config.WxCpTpConfigStorage;
import me.chanjar.weixin.cp.util.crypto.WxCpTpCryptUtil;
import me.chanjar.weixin.cp.util.xml.XStreamTransformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

/**
 * @author lixiang
 * @since 2021/5/8
 */
@RestController
@Slf4j
public class VerifyController {

    @Autowired
    private WXBizMsgCrypt providerMsgCrypt;

    @Autowired
    private WXBizMsgCrypt suitCorpMsgCrypt;

    @Autowired
    private WXBizMsgCrypt suitMsgCrypt;

    private WxCpTpService tpService;

    @Autowired
    private HandlerSelector<WxCpXmlOutMessage> handlerSelector;


    /**
     * 验证通用开发参数
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "callback_verify_provider" ,method = RequestMethod.GET)
    public String verifyProvider(VerifyRequest request) throws Exception {

        log.info("VerifyRequest -> [" + request + "]");

        // 需要返回的明文
        String sEchoStr="";
        PrintWriter out;
        try {
            sEchoStr = providerMsgCrypt.VerifyURL(request.getMsg_signature(), request.getTimestamp(),
                    request.getNonce(), request.getEchostr());
            log.info("verifyurl echostr: " + sEchoStr);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
        return sEchoStr;
    }

    /**
     * 数据回调url，应用中发送消息接收
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @RequestMapping("/callback/data")
    public String data(@RequestParam(name = "msg_signature", required = false) String signature,
                       @RequestParam(name = "timestamp", required = false) String timestamp,
                       @RequestParam(name = "nonce", required = false) String nonce,
                       @RequestParam(name = "echostr", required = false) String echostr,
                       HttpServletRequest request) {
        log.info("数据回调");
        log.info("signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]", signature, timestamp, nonce, echostr);
        tpService = WxCpTpConfiguration.getCpTpService(Constant.CRM_TEST_SUITE_ID);
        if (StringUtils.isEmpty(echostr)){
            try {
                BufferedReader reader = request.getReader();
                StringBuffer buffer = new StringBuffer();
                String line = " ";
                while (null != (line = reader.readLine())) {
                    buffer.append(line);
                }
                String postData = buffer.toString();
                String decryptMsgs = new WxCpTpCryptUtil(tpService.getWxCpTpConfigStorage()).decrypt(signature, timestamp, nonce, postData);
                System.out.println("数据回调-解密后的xml数据:" + decryptMsgs);
                WxCpTpXmlPackage tpXmlPackage = WxCpTpXmlPackage.fromXml(decryptMsgs);
                System.out.println(JSONUtil.toJsonStr(tpXmlPackage));

                // todo 添加责任链 避免使用if-else
                // 消息回复
                WxCpXmlOutMessage outMessage = handlerSelector.select(getMsgType(tpXmlPackage)).handle(tpXmlPackage, null);
                if (Objects.nonNull(outMessage)) {
                    String plainXml = XStreamTransformer.toXml((Class) outMessage.getClass(), outMessage);
                    log.info("\n组装回复信息：{}", plainXml);
                    WxCpTpCryptUtil pc = new WxCpTpCryptUtil(tpService.getWxCpTpConfigStorage());
                    return pc.encrypt(plainXml);
                }
                return "success";
            }catch (Exception e){
                log.error("校验失败：" + e.getMessage());
                return "success";
            }
        }
        try {
            if (tpService.checkSignature(signature, timestamp, nonce, echostr)) {
                return new WxCpTpCryptUtil(tpService.getWxCpTpConfigStorage()).decrypt(echostr);
            }
        } catch (Exception e) {
            log.error("校验签名失败：" + e.getMessage());
        }
        return "success";
    }

    private String getMsgType(WxCpTpXmlPackage message) {
        Map<String, Object> allFieldsMap = message.getAllFieldsMap();
        return allFieldsMap.get("MsgType").toString();
    }

    @RequestMapping(value = "/suite/receive")
    public String receiveSuiteTicket(@RequestParam("msg_signature") String signature,
                                     @RequestParam("timestamp") String timestamp,
                                     @RequestParam("nonce") String nonce,
                                     @RequestParam(value = "echostr", required = false) String echostr,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        log.info("指令回调URL-调用我了");
        log.info("signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]", signature, timestamp, nonce, echostr);

        tpService = WxCpTpConfiguration.getCpTpService(Constant.CRM_TEST_SUITE_ID);


        // 不为空为回调配置请求
        if (null != echostr) {
            try {
                if (tpService.checkSignature(signature, timestamp, nonce, echostr)) {
                    String decrypt = new WxCpTpCryptUtil(tpService.getWxCpTpConfigStorage()).decrypt(echostr);
                    log.info("指令回调URL结果：" + decrypt);
                    return decrypt;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        BufferedReader reader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String line = " ";
        while (null != (line = reader.readLine())) {
            buffer.append(line);
        }
        String postData = buffer.toString();
        String decryptMsgs = null;
        try {
            decryptMsgs = new WxCpTpCryptUtil(tpService.getWxCpTpConfigStorage()).decrypt(signature, timestamp, nonce, postData);
            System.out.println("解密后的xml数据:" + decryptMsgs);
        }catch (Exception e){
            log.error("校验失败：" + e.getMessage());
            return "success";
        }
        WxCpTpXmlMessage wxCpTpXmlMessage = WxCpTpXmlMessage.fromXml(decryptMsgs);
        InfoTypeEnum infoTypeEnum = InfoTypeEnum.getByInstance(wxCpTpXmlMessage.getInfoType());
        if (infoTypeEnum == null){
            throw new Exception("不支持该类型操作");
        }
        Map<String, Object> jsonObject = wxCpTpXmlMessage.getAllFieldsMap();
        switch (infoTypeEnum) {
            //（每十分钟）推送ticket。ticket会实时变更，并用于后续接口的调用。
            case SUITE_TICKET: {
                String suitId = wxCpTpXmlMessage.getSuiteId();
                String suiteTicket = wxCpTpXmlMessage.getSuiteTicket();
                Integer timeStamp = Convert.toInt(wxCpTpXmlMessage.getTimeStamp());
                log.info("推送ticket成功:" + jsonObject.toString());
                WxCpTpConfigStorage tpConfig = tpService.getWxCpTpConfigStorage();
                tpConfig.updateSuiteTicket(suiteTicket, 20*60);
                log.info("suit ticket缓存更新成功");

                String suitAccessToken = tpService.getSuiteAccessToken();
                log.info("suitAccessToken:" + suitAccessToken);
                break;
            }
            // 企业微信应用市场发起授权时，企业微信后台会推送授权成功通知
            case CREATE_AUTH:{
                log.info("创建授权：" + jsonObject.toString());
                String suiteId = wxCpTpXmlMessage.getSuiteId();
                String authCode = wxCpTpXmlMessage.getAuthCode();
                String timeStamp = wxCpTpXmlMessage.getTimeStamp();
                WxCpTpCorp permanentCode = tpService.getPermanentCode(authCode);
                log.info("永久授权码：" + permanentCode);
                break;
            }
            case CHANGE_AUTH:{
                log.info("变更授权");
                // TODO: 2020/2/14
                break;
            }
           /*case CHANGE_CONTACT:{
                logger.info("通讯录变更");
                String changeType = jsonObject.get(WxCpTpInnerConstant.CHANGE_TYPE).toString();
                ChangeTypeEnum changeTypeEnum = ChangeTypeEnum.getByInstance(changeType);
                if (Objects.isNull(changeTypeEnum)){
                    throw new Exception("该类型通讯录变更不存在");
                }
                switch (changeTypeEnum){
                    case DELETE_USER:{
                        logger.info("删除用户");
                        break;
                    }
                    case UPDATE_TAG:{
                        logger.info("更新标签");
                        break;
                    }
                    case DELETE_PARTY:{
                        logger.info("删除部门");
                        break;
                    }
                    default:{
                        break;
                    }
                }
                break;
            }
            case CHANGE_EXTERNAL_CONTACT:{
                logger.info("外部联系人");
                String changeType = jsonObject.get(WxCpTpInnerConstant.CHANGE_TYPE).toString();
                ChangeTypeEnum changeTypeEnum = ChangeTypeEnum.getByInstance(changeType);
                if (Objects.isNull(changeTypeEnum)){
                    throw new Exception("该类型CHANGE_EXTERNAL_CONTACT不存在");
                }
                switch (changeTypeEnum){
                    case ADD_EXTERNAL_CONTACT:{
                        logger.info("添加客户" + jsonObject.toString());
                        String externalUserID = jsonObject.get("ExternalUserID").toString();
                        String welcomeCode = jsonObject.get("WelcomeCode").toString();
                        if (StringUtils.isEmpty(welcomeCode)){
                            logger.warn("welcomeCode没获取到，用户可能已经设置了欢迎语！！！");
                            break;
                        }
                        String userID = jsonObject.get("UserID").toString();
                        String suiteId = jsonObject.get("SuiteId").toString();
                        String authCorpId = jsonObject.get("AuthCorpId").toString();
                        System.out.println("欢迎code：" + welcomeCode);
                        sendWelcome(tpService, welcomeCode);
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }*/
            default: {
                break;
            }
        }
        return "success";

        /*// 密文，对应POST请求的数据
        String postData="";
        //1.获取加密的请求消息：使用输入流获得加密请求消息postData
        ServletInputStream in = request.getInputStream();
        BufferedReader reader =new BufferedReader(new InputStreamReader(in));

        //作为输出字符串的临时串，用于判断是否读取完毕
        String tempStr="";
        while(null!=(tempStr=reader.readLine())){
            postData += tempStr;
        }

        String suiteXml=suitMsgCrypt.DecryptMsg(signature, timestamp, nonce, postData);
        log.info("suiteXml: " + suiteXml);

        Map suiteMap = WxUtil.parseXml(suiteXml);
        if(suiteMap.get("SuiteTicket") != null) {
           log.info("receive SuiteTicket : " + suiteMap.get("SuiteTicket"));
            QyWechatCache.put("SuiteTicket", (String) suiteMap.get("SuiteTicket"));
        } else if(suiteMap.get("AuthCode") != null){
            log.info("receive AuthCode : " + suiteMap.get("AuthCode"));
        }
        String success = "success";
        return success;*/
    }
}
