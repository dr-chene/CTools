package com.example.chttp;

/**
 * Created by dr-chene on @date 2020/4/4
 */
public class CHttp {
    private static CHttp chHttp;
    private static CHttpClient chHttpClient;

    private CHttp() {
        chHttpClient = new CHttpClient();
    }

    public static CHttp getChHttp() {
        if (chHttp == null) {
            synchronized (CHttp.class){
                if (chHttp == null){
                    chHttp = new CHttp();
                }
            }
        }
        return chHttp;
    }

    public CHttp newCall(Request request, CallBack callBack) {
        chHttpClient.newCall(request, callBack);
        return this;
    }

    public void execute() {
        chHttpClient.execute();
    }
}
