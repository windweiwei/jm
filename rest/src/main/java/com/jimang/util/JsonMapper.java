package com.jimang.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fashionbrot.validated.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Auther:wind
 * @Date:2020/7/20
 * @Version 1.0
 */
@Slf4j
public final class JsonMapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final byte[] EMPTY_BYTES = new byte[0];

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private JsonMapper() {
    }

    public static byte[] writeValueAsBytes(Object object) {
        byte[] json = EMPTY_BYTES;
        try {
            json = MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            log.warn("object to json bytes error", e);
        }
        return json;
    }

    public static String writeValueAsString(Object object) {
        String json = "";
        try {
            json = MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("object to json string error", e);
        }
        return json;
    }

    public static <T> T readValue(byte[] data, Class<T> clazz) {
        T t = null;
        if (data != null && data.length != 0) {
            try {
                t = MAPPER.readValue(data, clazz);
            } catch (IOException e) {
                log.warn("json bytes to object error", e);
            }
        }
        return t;
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        T t = null;
        if (StringUtil.isNotEmpty(json)) {
            try {
                t = MAPPER.readValue(json, clazz);
            } catch (IOException e) {
                log.warn("json string to object error", e);
            }
        }
        return t;
    }

    public static <T> T readValue(String json, TypeReference valueTypeRef) {
        T t = null;
        if (StringUtil.isNotEmpty(json)) {
            try {
                t = MAPPER.readValue(json, (TypeReference<T>) valueTypeRef);
            } catch (IOException e) {
                log.warn("json string to object error", e);
            }
        }
        return t;
    }
}