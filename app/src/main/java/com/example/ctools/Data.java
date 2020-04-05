package com.example.ctools;

import androidx.annotation.NonNull;

/**
 * Created by dr-chene on @date 2020/4/5
 */
public class Data {
    private DataArray data;

    public DataArray getData() {
        return data;
    }

    public void setData(DataArray data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return data.toString();
    }
}
