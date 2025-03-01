package com.onedongua.abutton.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class JsonUtils {

    // 创建一个 ObjectMapper 实例，用于进行序列化和反序列化
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    /**
     * 将对象转换为 JSON 字符串
     *
     * @param obj 待转换的对象
     * @return JSON 字符串
     * @throws JsonProcessingException 如果转换失败
     */
    public static String toJson(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 将 JSON 字符串转换为对象
     *
     * @param json  JSON 字符串
     * @param clazz 目标类型
     * @param <T>   泛型
     * @return 转换后的对象
     * @throws IOException 如果转换失败
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return objectMapper.readValue(json, clazz);
    }

    /**
     * 将 JSON 字符串转换为对象（使用通用类型）
     *
     * @param json JSON 字符串
     * @param type 目标类型
     * @param <T>  泛型
     * @return 转换后的对象
     * @throws IOException 如果转换失败
     */
    public static <T> T fromJson(String json, TypeReference<T> type) throws IOException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return objectMapper.readValue(json, type);
    }

    /**
     * 将 JSON 字符串转换为 Map
     *
     * @param json JSON 字符串
     * @return Map<String, Object>
     * @throws IOException 如果转换失败
     */
    public static Map<String, Object> fromJsonToMap(String json) throws IOException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

    /**
     * 从文件读取 JSON 并转换为对象
     *
     * @param file  文件
     * @param clazz 目标类型
     * @param <T>   泛型
     * @return 转换后的对象
     * @throws IOException 如果读取文件或转换失败
     */
    public static <T> T fromJsonFile(File file, Class<T> clazz) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            return objectMapper.readValue(fis, clazz);
        }
    }

    /**
     * 从文件读取 JSON 并转换为对象（使用通用类型）
     *
     * @param file 文件
     * @param type 目标类型
     * @param <T>  泛型
     * @return 转换后的对象
     * @throws IOException 如果读取文件或转换失败
     */
    public static <T> T fromJsonFile(File file, TypeReference<T> type) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            return objectMapper.readValue(fis, type);
        }
    }

    /**
     * 将对象转换为 JSON 并写入文件
     *
     * @param obj  对象
     * @param file 文件
     * @throws IOException 如果写入文件失败
     */
    public static void toJsonFile(Object obj, File file) throws IOException {
        if (obj == null || file == null) {
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            objectMapper.writeValue(fos, obj);
        }
    }
}
