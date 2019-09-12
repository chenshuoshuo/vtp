package com.you07.eas.model;

import java.util.List;

public class Result<T> {
    private Boolean status;
    private Integer code;
    private String message;
    private Long time;
    private T data;
    private T properties;

    public Result() {
    }

    public Result(Long time, Boolean status, Integer code, String message, T data, T properties) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.time = time;
        this.properties = properties;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T properties) {
        this.properties = properties;
    }
}

