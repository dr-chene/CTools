package com.example.cflowlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dr-chene on @date 2020/4/4
 */
public class CFlowLayout extends FrameLayout {

    private static final int H_DISTANCE = 20;
    private static final int V_DISTANCE = 20;
    private List<DataBean> tags;

    public CFlowLayout(@NonNull Context context) {
        super(context);
    }

    public CFlowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CFlowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CFlowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    public void addTextView(String keys,CallBackImp callBackImp){
        if (tags == null){
            tags = new ArrayList<>();
        }
        keys = keys.replaceAll("\r\n|\r|\n", "");
        tags.add(new DataBean(keys,callBackImp));
        reaAddTextView();
    }

    private void reaAddTextView(){
        removeAllViews();
        for (int i = tags.size() - 1; i >= 0 ; i--) {
           final DataBean bean = tags.get(i);
            TextView textView = (TextView) View.inflate(getContext(),R.layout.flowlayout_item_tv,null);
            String text;
            if (bean.getString().length() <= 19){
                text = bean.getString();
            }else {
                text = bean.getString().substring(0,18)+"...";
            }
            textView.setText(text);
            LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   bean.getCallBackImp().callBack(bean.getString());
                }
            });
            addView(textView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getWidth();
        int row = 0;
        int disWidth = H_DISTANCE;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childrenWidth = child.getWidth();
            int childrenHeight = child.getHeight();
            if (childrenWidth + disWidth > width) {
                row++;
                disWidth = H_DISTANCE;
            }
            child.layout(disWidth,
                    row * V_DISTANCE + row * childrenHeight,
                    disWidth + childrenWidth,
                    row * V_DISTANCE + row * childrenHeight + childrenHeight);
            disWidth += (childrenWidth + H_DISTANCE);
        }
    }
}
