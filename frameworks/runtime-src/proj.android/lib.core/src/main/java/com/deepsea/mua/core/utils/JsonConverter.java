package com.deepsea.mua.core.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JUN on 2019/3/22
 */
public class JsonConverter {

    /**
     * json转list 保留类型
     *
     * @param s
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getListFromJSON(String s, Class<T[]> clazz) throws JsonSyntaxException {
        T[] arr = new GsonBuilder().create().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    /**
     * object转json
     *
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        return new GsonBuilder().create().toJson(src);
    }

    /**
     * json转object
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonSyntaxException {
        return new GsonBuilder().create().fromJson(json, clazz);
    }
}
