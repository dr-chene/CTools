package com.example.ctools;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cflowlayout.CFlowLayout;
import com.example.cflowlayout.CallBackImp;
import com.example.chttp.CHttp;
import com.example.chttp.CallBack;
import com.example.chttp.Request;
import com.example.chttp.RequestBody;
import com.example.cjson.CJson;
import com.example.csearchview.SearchViewBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivityDebug";

    private Button btn;
    private CFlowLayout cFlowLayout;
    private SearchViewBar searchViewBar;
    private CHttp http;
    private CJson json;
    private Data data = new Data();
    private ScrollView scrollView;
    private List<Class> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        http = CHttp.getChHttp();
        classes = new ArrayList<>();
        classes.add(DataBean.class);
        json = new CJson(classes);
        searchViewBar = findViewById(R.id.sv);
        btn = findViewById(R.id.btn);
        cFlowLayout = findViewById(R.id.view);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String keys = searchView.getText().toString();
//                if (!keys.isEmpty()) {
//                    Toast toast = Toast.makeText(MainActivity.this, keys, Toast.LENGTH_SHORT);
//                    toast.show();
//                    cFlowLayout.addTextView(keys, new CallBackImp() {
//                        @Override
//                        public void callBack(String key) {
//                            Toast.makeText(MainActivity.this, key, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    searchView.setText("");
//                }
//            }
//        });

        searchViewBar.setBackListener(new SearchViewBar.BackListener() {
            @Override
            public void onBack() {
//                onBackPressed();
                Toast.makeText(MainActivity.this,"点击了back键",Toast.LENGTH_SHORT).show();
            }
        });
        searchViewBar.setSearchListener(new SearchViewBar.SearchListener() {
            @Override
            public void onSearch(String text) {
//                Toast.makeText(MainActivity.this,"search start",Toast.LENGTH_SHORT).show();
                Request request = new Request(text);
                http.newCall(request, new CallBack() {
                    @Override
                    public void onSuccess(String str) {
//                        Log.d(TAG, "onSuccess: " + str);
                        data = json.formJson(str,data.getClass());
//                        Looper.prepare();
//                        Toast.makeText(MainActivity.this,data.toString(),Toast.LENGTH_LONG).show();
//                        Looper.loop();
                        changeData();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this,"data get failed!",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Looper.loop();
                    }
                });
                http.execute();
            }
        });
    }

    private void changeData() {
        Looper.prepare();
        Toast.makeText(MainActivity.this,json.toJson(data),Toast.LENGTH_SHORT).show();
        Looper.loop();
        //GET
//        CHttp cHttp = CHttp.getChHttp();
//        String url = "";
//        Request request = new Request(url);
//        request.addHeader("key","value");
//        http.newCall(request, new CallBack() {
//            @Override
//            public void onSuccess(String str) {
//
//            }
//
//            @Override
//            public void onFailed(Exception e) {
//
//            }
//        });
//        http.execute();

//        //POST
//        RequestBody requestBody = new RequestBody().add("key","value").add("key","value");
//        request.addHeader("key","value");
//        request.post(requestBody);
//        http.newCall(request, new CallBack() {
//            @Override
//            public void onSuccess(String str) {
//
//            }
//
//            @Override
//            public void onFailed(Exception e) {
//
//            }
//        });
//        http.execute();
    }

}
