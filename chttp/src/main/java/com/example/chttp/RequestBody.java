package com.example.chttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dr-chene on @date 2020/4/4
 */
public class RequestBody {
    private Map<String, String> params;

    public RequestBody() {
        this.params = new HashMap<>();
    }

    public RequestBody add(String key, String value) {
        params.put(key, value);
        return this;
    }

    Map<String, String> getParams() {
        return params;
    }
}
