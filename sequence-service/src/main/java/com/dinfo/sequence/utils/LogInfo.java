package com.dinfo.sequence.utils;

import com.dinfo.common.model.Response;

/**
 * 纪录日志信息
 * Created by winston.wang on 2016/11/23.
 */
class LogInfo {

    /**
     * resId
     */
    private String resId;
    /**
     * 请求url
     */
    private String url;

    /**
     * http method
     */
    private String httpMethod;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 处理类和方法
     */
    private String classMethod;

    /**
     * 参数
     */
    private String args;

    /**
     * 纪录时间
     */
    private Long time;

    /**
     * 返回信息
     */
    Response response;

    /**
     * 错误堆栈
     */
    private String errStack;

    private LogInfo(){

    }
    static LogInfo build(){
        return new LogInfo();
    }

    LogInfo setResId(String resId) {
        this.resId = resId;
        return this;
    }

    LogInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    LogInfo setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    LogInfo setIp(String ip) {
        this.ip = ip;
        return this;
    }


    LogInfo setClassMethod(String classMethod) {
        this.classMethod = classMethod;
        return this;
    }

    LogInfo setArgs(String args) {
        this.args = args;
        return this;
    }

    LogInfo setTime(Long time) {
        this.time = time;
        return this;
    }

    LogInfo setErrStack(String errStack) {
        this.errStack = errStack;
        return this;
    }

    LogInfo setResponse(Response response) {
        this.response = response;
        return this;
    }

    @Override
    public String toString() {
        return "LogInfo{" +
                "resId='" + resId + '\'' +
                ", url='" + url + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", ip='" + ip + '\'' +
                ", classMethod='" + classMethod + '\'' +
                ", args='" + args + '\'' +
                ", time=" + time +
                ", response=" + response +
                ", errStack='" + errStack + '\'' +
                '}';
    }
}
