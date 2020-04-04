package com.example.cjson;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by dr-chene on @date 2020/4/4
 */
public class CJson {
    private static final Class[] classes = {
            byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class,
            Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class};

    public String toJson(@NonNull Object bean) {
        try {
            return fromObject(bean).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private JSONObject fromObject(Object bean) throws Exception {
        if (bean == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String getterName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            Method getter = bean.getClass().getDeclaredMethod(getterName);
            getter.setAccessible(true);
            if (field.getType() == List.class) {
                jsonObject.put(field.getName(), fromList((List) getter.invoke(bean)));
            } else if (isBasicType(field.getType())) {
                jsonObject.put(field.getName(), getter.invoke(bean));
            } else {
                jsonObject.put(field.getName(), fromObject(Objects.requireNonNull(getter.invoke(bean))));
            }
        }
        return jsonObject;
    }

    private JSONArray fromList(List<Object> list) throws Exception {
        JSONArray jsonArray = new JSONArray();
        if (list == null) {
            return jsonArray;
        }
        for (Object obj : list) {
            if (obj instanceof List) {
                jsonArray.put(fromList((List) obj));
            } else if (isBasicType(obj.getClass())) {
                jsonArray.put(obj);
            } else {
                jsonArray.put(fromObject(obj));
            }
        }
        return jsonArray;
    }

    private boolean isBasicType(Class type) {
        for (Class clazz : classes) {
            if (type == clazz) {
                return true;
            }
        }
        return false;
    }

    public <T> T formJson(String json, Class<T> clazz) {
        if (clazz == List.class) {
            return null;
        }
        try {
            return fromObject(new JSONObject(json), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> T fromObject(JSONObject jsonObject, Class<T> clazz) throws Exception {
        T result = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            Method setter = clazz.getDeclaredMethod(setterName, field.getType());
            setter.setAccessible(true);
            Object element;
            if (field.getType() == List.class) {
                element = fromArray(jsonObject.getJSONArray(field.getName()));
            } else if (isBasicType(field.getType())) {
                element = jsonObject.get(field.getName());
            } else {
                element = fromObject(jsonObject.getJSONObject(field.getName()), field.getType());
            }
            setter.invoke(result, element);
        }
        return result;
    }

    private <T> List fromArray(JSONArray jsonArray) throws Exception {
        List<Object> list = new ArrayList<>();
        if (jsonArray.length() == 0) {
            return list;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            Object obj = jsonArray.get(i);
            if (obj instanceof JSONArray) {
                list.add(fromArray((JSONArray) obj));
            } else if (obj instanceof JSONObject) {
                list.add(fromObject(obj));
            } else {
                list.add(obj);
            }
        }
        return list;
    }
}
