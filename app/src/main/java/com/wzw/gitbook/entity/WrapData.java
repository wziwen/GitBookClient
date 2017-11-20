package com.wzw.gitbook.entity;

/**
 * Created by ziwen.wen on 2017/11/14.
 */
public class WrapData <T> {
    private final int SUCCESS = -0xff;

    public int errorCode;
    public String msg;
    public T data;

    public WrapData(T data) {
        errorCode = SUCCESS;
        this.data = data;
    }

    public WrapData(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }
}
