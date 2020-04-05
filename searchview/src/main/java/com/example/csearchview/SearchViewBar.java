package com.example.csearchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by dr-chene on @date 2020/4/5
 */
public class SearchViewBar extends FrameLayout implements View.OnClickListener {

    private ImageView searchViewBarBackIV;
    private ImageView searchViewBarClearIV;
    private ImageView searchViewBarSearchIV;
    private EditText searchViewBarET;
    private View mView;

    private String ETHint;
    private int ETColor;

    private SearchListener searchListener;
    private BackListener backListener;

    public SearchViewBar(@NonNull Context context) {
        super(context);
    }

    public SearchViewBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchViewBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SearchViewBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initView(context);
        setData();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchViewBar);
        ETHint = typedArray.getString(R.styleable.SearchViewBar_SearchViewBarETHint);
        ETColor = typedArray.getColor(R.styleable.SearchViewBar_SearchViewBarETColor, Color.parseColor("#8a8a8a"));
        typedArray.recycle();
    }

    private void initView(final Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.search_bar_layout, null);
        searchViewBarET = mView.findViewById(R.id.search_bar_et);
        searchViewBarBackIV = mView.findViewById(R.id.search_bar_back_iv);
        searchViewBarClearIV = mView.findViewById(R.id.search_bar_clear_iv);
        searchViewBarSearchIV = mView.findViewById(R.id.search_bar_search_iv);
        searchViewBarClearIV.setOnClickListener(this);
        searchViewBarBackIV.setOnClickListener(this);
        searchViewBarSearchIV.setOnClickListener(this);
        searchViewBarET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    notifyStartSearch(searchViewBarET.getText().toString(), context);
                }
                return true;
            }
        });
        searchViewBarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                onETChanged(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onETChanged(s.toString());
            }
        });
        addView(mView);

    }

    private void onETChanged(String text) {
        if (text.length() == 0) {
            searchViewBarClearIV.setVisibility(GONE);
        } else {
            searchViewBarClearIV.setVisibility(VISIBLE);
        }
    }

    private void setData() {
        searchViewBarET.setHint(ETHint);
        searchViewBarET.setTextColor(ETColor);
    }

    public void notifyStartSearch(String text, Context context) {
        if (searchListener != null) {
            searchListener.onSearch(text);
        }
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.search_bar_back_iv) {
            if (backListener != null) {
                backListener.onBack();
            }
        } else if (id == R.id.search_bar_clear_iv) {
            if (searchViewBarClearIV.getVisibility() != GONE) {
                searchViewBarET.setText("");
            }
        } else if (id == R.id.search_bar_search_iv) {
            if (searchListener != null) {
                searchListener.onSearch(searchViewBarET.getText().toString());
            }
        }
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public void setBackListener(BackListener backListener) {
        this.backListener = backListener;
    }

    public interface SearchListener {
        void onSearch(String text);
    }

    public interface BackListener {
        void onBack();
    }
}
