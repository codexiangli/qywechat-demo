package com.codeliliu.qywechatdemo;

/**
 * 消息指令
 * @author lixiang
 * @since 2021/5/10
 */
public enum InfoTypeEnum {

    SUITE_TICKET("suite_ticket", "推送suite_ticket"),
    CREATE_AUTH("create_auth", "授权成功通知"),
    CHANGE_AUTH("change_auth", "变更授权通知"),
    CANCEL_AUTH("cancel_auth", "取消授权通知"),
    CHANGE_CONTACT("change_contact", "通讯录事件"),
    CHANGE_EXTERNAL_CONTACT("change_external_contact", "外部联系人事件");

    InfoTypeEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static InfoTypeEnum getByInstance(String key){
        for (InfoTypeEnum value : InfoTypeEnum.values()) {
            if (value.key.equalsIgnoreCase(key)){
                return value;
            }
        }
        return null;
    }

    private String key;

    private String desc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
