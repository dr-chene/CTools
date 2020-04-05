package com.example.chttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dr-chene on @date 2020/4/4
 * use method:
 * GET:
 * CHttp Http = CHttp.getCHttp();
 * Request request = new Request(url);
 * (可选)request.addHeader("key","value");
 * http.newCall(request, new CallBack(){});
 * http.execute();
 * POST:
 * CHttp Http = CHttp.getCHttp();
 * Request request = new Request(url);
 * RequestBody requestBody = new RequestBody().add("key","value").add("key","value");
 * (可选)request.addHeader("key","value");
 * request.post(requestBody);
 * http.newCall(request, new CallBack(){});
 * http.execute();
 */
public class CHttp {
    private static CHttp chHttp;
    private static CHttpClient chHttpClient;
    private Map<Request, CallBack> map;

    private CHttp() {
        chHttpClient = new CHttpClient();
    }

    public static CHttp getChHttp() {
        if (chHttp == null) {
            synchronized (CHttp.class) {
                if (chHttp == null) {
                    chHttp = new CHttp();
                }
            }
        }
        return chHttp;
    }

    public CHttp newCall(Request request, CallBack callBack) {
        if (map == null) {
            synchronized (CHttp.class) {
                if (map == null) {
                    map = new HashMap<>();
                }
            }
        }
        map.put(request, callBack);
        return this;
    }

    public void execute() {
        for (Map.Entry<Request, CallBack> e :
                map.entrySet()) {
            chHttpClient.newCall(e.getKey(), e.getValue());
            chHttpClient.execute();
        }
        map.clear();
    }
}
