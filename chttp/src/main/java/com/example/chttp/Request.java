package com.example.chttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dr-chene on @date 2020/4/4
 */
public class Request {
    private String url;
    private String method;
    private RequestBody body;
    private Map<String, String> headers;

    private Request() {
        this.method = "GET";
        headers = new HashMap<>();
    }

    public Request(String url) {
        this();
        this.url = url;
    }

    public void post(RequestBody body) {
        method = "POST";
        this.body = body;
    }

    String getMethod() {
        return method;
    }

    String getUrl() {
        return url;
    }

    RequestBody getBody() {
        return body;
    }

    Map<String, String> getHeaders() {
        return headers;
    }

    public Request addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Request addHeaders(Headers clazz) {
        Map<String, String> addingHeaders = clazz.getHeaders();
        for (Map.Entry<String, String> e :
                addingHeaders.entrySet()) {
            headers.put(e.getKey(), e.getValue());
        }
        return this;
    }
}
