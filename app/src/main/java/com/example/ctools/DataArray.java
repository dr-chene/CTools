package com.example.ctools;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created by dr-chene on @date 2020/4/5
 */
public class DataArray {
    private int curPage;
    private List<DataBean> datas;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<DataBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DataBean> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("curPage:"+curPage+" ");
        for (int i = 0; i < datas.size(); i++) {
            sb.append(" "+datas.get(i).toString());
        }
        return sb.toString();
    }
}
