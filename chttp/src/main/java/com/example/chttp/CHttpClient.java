package com.example.chttp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dr-chene on @date 2020/4/4
 */
public class CHttpClient {
    private Thread thread;
    private Boolean executed;
    private ExecutorService executorService;

    CHttpClient() {
        executorService = Executors.newCachedThreadPool();
        executed = false;
    }

    void newCall(final Request request, final CallBack callBack) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(request.getUrl());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(request.getMethod());
                    if (request.getMethod().equals("POST")) {
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        out.writeBytes(post(request.getBody().getParams()));
                    }
                    if (request.getHeaders() != null && request.getHeaders().size() > 0) {
                        addHeaders(connection, request.getHeaders());
                    }
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (callBack != null) {
                        callBack.onSuccess(response.toString());
                    }
                } catch (Exception e) {
                    if (callBack != null) {
                        callBack.onFailed(e);
                    }
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
    }

    private void addHeaders(HttpURLConnection connection, Map<String, String> headers) {
        for (Map.Entry<String, String> e :
                headers.entrySet()) {
            connection.setRequestProperty(e.getKey(), e.getValue());
        }
    }

    void execute() {
        synchronized (this) {
            if (!executed) {
                executed = true;
                executorService.execute(thread);
                executed = false;
            }
        }
    }

    private String post(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                sb.append(e.toString());
                sb.append("&");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
