package com.example.cflowlayout;

/**
 * Created by dr-chene on @date 2020/4/4
 */
public class DataBean {
    private String string;
    private CallBackImp callBackImp;

     DataBean(String string, CallBackImp callBackImp) {
        this.string = string;
        this.callBackImp = callBackImp;
    }

     String getString() {
        return string;
    }

     CallBackImp getCallBackImp() {
        return callBackImp;
    }
}
