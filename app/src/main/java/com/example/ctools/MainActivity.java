package com.example.ctools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cflowlayout.CFlowLayout;
import com.example.cflowlayout.CallBackImp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText searchView;
    Button btn;
    CFlowLayout cFlowLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        searchView = findViewById(R.id.sv);
        btn = findViewById(R.id.btn);
        cFlowLayout= findViewById(R.id.view);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keys =  searchView.getText().toString();
                if (!keys.isEmpty()) {
                   Toast toast = Toast.makeText(MainActivity.this,keys,Toast.LENGTH_SHORT);
                   toast.show();
                   cFlowLayout.addTextView(keys, new CallBackImp() {
                       @Override
                       public void callBack(String key) {
                           Toast.makeText(MainActivity.this,key,Toast.LENGTH_SHORT).show();
                       }
                   });
                searchView.setText("");
                }
            }
        });
    }

}
