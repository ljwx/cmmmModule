package com.ljwx.sib.base.src.main.java.com.sisensing.http;

/**
 * 实际业务返回的固定字段, 根据需求来定义，
 */
public class BaseResponse<T,U> extends com.ljwx.baseapp.response.BaseResponse<T> {
    //    private int code;
//    private String msg;
    private long timestamp;
    //    private T data;
    private U errorData;
    private boolean success;

//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }

    public U getErrorData() {
        return errorData;
    }

    public void setErrorData(U errorData) {
        this.errorData = errorData;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isOk() {
        return getCode() == 200;
    }
}
