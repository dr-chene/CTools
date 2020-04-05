package com.example.cjson;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by dr-chene on @date 2020/4/4
 * 使用说明：由于无法得到jsonObject的具体类类型，
 * 需要在构建类时传入classList
 * 参数为bean及其子类中所以的List<T>中的参数T
 * 用同一实例转换不同的json时请重新设置classList域
 */
public class CJson {

    private final Class[] classes = {
            byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class,
            Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class,
            char.class, Character.class, String.class};
    private List<Class> clazzList;
    private int location;

    public CJson(List<Class> clazzList) {
        this.clazzList = clazzList;
    }

    public void setClazzList(List<Class> clazzList) {
        this.clazzList = clazzList;
    }

    public String toJson(@NonNull Object bean) {
        try {
            return fromObject(bean).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private JSONObject fromObject(Object object) throws Exception {
        if (object == null) {
            return null;
        }
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        JSONObject jsonObject = new JSONObject();
        for (Field field : fields) {
            String getterName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            Method getter = clazz.getDeclaredMethod(getterName);
            getter.setAccessible(true);
            if (field.getType() == List.class) {
                jsonObject.put(field.getName(), fromList((List) getter.invoke(object)));
            } else if (isBasicType(field.getType())) {
                jsonObject.put(field.getName(), getter.invoke(object));
            } else {
                jsonObject.put(field.getName(), fromObject(Objects.requireNonNull(getter.invoke(object))));
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

    public <T> T formJson(@NonNull String json, Class<T> clazz) {
        location = 0;
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

    private <T> T fromObject(JSONObject object, Class<T> clazz) throws Exception {
        T result = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            Method setter = clazz.getDeclaredMethod(setterName, field.getType());
            setter.setAccessible(true);
            Object element;
            if (field.getType() == List.class) {
                try {
                    object.getJSONArray(field.getName());
                } catch (JSONException e) {
                    continue;
                }
                element = fromArray(object.getJSONArray(field.getName()));
            } else if (isBasicType(field.getType())) {
                try {
                    object.get(field.getName());
                } catch (JSONException e) {
                    continue;
                }
                element = object.get(field.getName());
            } else {
                element = fromObject(object.getJSONObject(field.getName()), field.getType());
            }
            setter.invoke(result, element);
        }
        return result;
    }

    private <T> List fromArray(JSONArray jsonArray) throws Exception {
        List<Object> result = new ArrayList<>();
        if (jsonArray.length() == 0) {
            return result;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            Object obj = jsonArray.get(i);
            if (obj instanceof JSONArray) {
                result.add(fromArray((JSONArray) obj));
            } else if (obj instanceof JSONObject) {
                if (location >= clazzList.size()) {
                    throw new Exception("clazzList error!");
                }
                result.add(fromObject((JSONObject) obj, clazzList.get(location)));
            } else {
                result.add(obj);
            }
        }
        return result;
    }


}
