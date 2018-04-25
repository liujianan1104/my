package com.dinfo.bid.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;

/**
 * User: Adonis wu
 * Date: 2018/4/20
 * Desc:    OEC 结果封装类，存储信息抽取结果，存储C分类结果，存储token
 */
public class OECResult implements Serializable {

    //  存储token值
    private String token = null;

    //  存储信息抽取返回结果
    private Map<String, Object> extractMap = new HashMap<String, Object>();

    //  存储C分类返回结果
    private Map<String, Object> cClassMap = new HashMap<String, Object>();

    //  错误情况下返回的状态信息
    private String message;

    //  获取token的时间
    private Long time;

    //  token 失效时间
    private Long tokenSession = 30L * 60000;

    //  状态
    private boolean status;

    //  实体识别结果
    private String nerContent;

    //  oec 接口返回信息
    private String oecResult;

    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Object> getExtractMap() {
        return extractMap;
    }

    public void setExtractMap(Map<String, Object> extractMap) {
        this.extractMap = extractMap;
    }

    public Map<String, Object> getcClassMap() {
        return cClassMap;
    }

    public void setcClassMap(Map<String, Object> cClassMap) {
        this.cClassMap = cClassMap;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTokenSession() {
        return tokenSession;
    }

    public String getNerContent() {
        return nerContent;
    }

    public void setNerContent(String nerContent) {
        this.nerContent = nerContent;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOecResult() {
        return oecResult;
    }

    public void setOecResult(String oecResult) {
        this.oecResult = oecResult;
    }
}
