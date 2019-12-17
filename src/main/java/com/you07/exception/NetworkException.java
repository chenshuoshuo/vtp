package com.you07.exception;

/**
 * @author egan
 * @date 2019/12/17 15:26
 * @desc
 */
public class NetworkException extends RuntimeException {

    public NetworkException(Exception e) {
        super(e);
    }
}
