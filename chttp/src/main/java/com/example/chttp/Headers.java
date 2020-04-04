package com.example.chttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dr-chene on @date 2020/4/4
 */
public class Headers {
    private Map<String, String> headers;

    public Headers() {
        headers = new HashMap<>();
    }

    public Headers addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Headers removeHeader(String key) {
        headers.remove(key);
        return this;
    }

    Map<String, String> getHeaders() {
        return headers;
    }
}
