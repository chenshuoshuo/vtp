package com.you07.util.message;

import java.io.Serializable;

/**
 * 服务器单个信息bean
 * Created by free on 2017/7/27 0027.
 */
public class MessageBean<T>  extends MessageBaseBean implements Serializable {

    public MessageBean() {
        this.setTime(System.currentTimeMillis());
    }

    public static MessageBean ok(){
        MessageBean messageBean = new MessageBean();
        messageBean.setStatus(true);
        messageBean.setCode(200);
        messageBean.setMessage("成功");
        return messageBean;
    }

    public MessageBean(T data) {
        this.data = data;

        this.setTime(System.currentTimeMillis());
    }

    /**
     * 被包含的消息实体
     */
    private T data;

    public static MessageBean error(String msg) {
        MessageBean messageBean = new MessageBean();
        messageBean.setStatus(true);
        messageBean.setCode(200);
        messageBean.setMessage(msg);
        return messageBean;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
