package org.esni.siskin_core.bean;

import java.io.Serializable;
import java.util.Map;

public class SerializeData implements Serializable {

    private String request;
    private String method;
    private String remoteAddress;
    private String requestParameter;
    private String contentType;
    private String cookie;
    private String serverAddress;
    private String Referer;
    private String userAgent;
    private String timeISO8601;
    private String timeLocal;
    private Map<String, Object> metaData;

    public SerializeData() {

    }

    public SerializeData(String request, String method, String remoteAddress, String requestParameter, String contentType, String cookie, String serverAddress, String referer, String userAgent, String timeISO8601, String timeLocal, Map<String, Object> metaData) {
        this.request = request;
        this.method = method;
        this.remoteAddress = remoteAddress;
        this.requestParameter = requestParameter;
        this.contentType = contentType;
        this.cookie = cookie;
        this.serverAddress = serverAddress;
        Referer = referer;
        this.userAgent = userAgent;
        this.timeISO8601 = timeISO8601;
        this.timeLocal = timeLocal;
        this.metaData = metaData;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(String requestParameter) {
        this.requestParameter = requestParameter;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getReferer() {
        return Referer;
    }

    public void setReferer(String referer) {
        Referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getTimeISO8601() {
        return timeISO8601;
    }

    public void setTimeISO8601(String timeISO8601) {
        this.timeISO8601 = timeISO8601;
    }

    public String getTimeLocal() {
        return timeLocal;
    }

    public void setTimeLocal(String timeLocal) {
        this.timeLocal = timeLocal;
    }

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
        this.metaData = metaData;
    }

}
