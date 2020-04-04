package com.example.chttp;

/**
 * Created by dr-chene on @date 2020/4/4
 */
public interface CallBack {
    void onSuccess(String str);

    void onFailed(Exception e);
}
