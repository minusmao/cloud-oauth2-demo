package com.example.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基于jackson框架的JSON工具类<br>
 * 参考文档1：<a href="https://blog.csdn.net/pan_junbiao/article/details/106442666">Jackson的使用与创建Jackson工具类</a><br>
 * 参考文档2：<a href="https://juejin.cn/post/6844904166809157639">Jackson使用详解</a><br>
 *
 * @author minus
 * @since 2022/12/8 23:02
 */
@Slf4j
public class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = generateObjectMapper();
    }

    /**
     * 获得指定规则的ObjectMapper对象
     *
     * @return ObjectMapper对象
     */
    public static ObjectMapper generateObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 配置忽略未知字段，即JSON中的字段在Java对象中找不到，直接忽略，不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 设置基本类型不能为null值，默认基本类型在JSON为null时，会自动处理，这里可以设置为不能为null且抛出异常
//        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        // 设置LocalDateTime序列化支持，参考文档：https://juejin.cn/post/7025160932653268999
        // 设置LocalDateTime序列化支持，2022-12-18修改，此方法更方便，参考文档：https://blog.csdn.net/sswltt/article/details/108868480
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    /**
     * 将对象转换成JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String beanToJsonStr(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转 JSON 字符串异常", e);
        }
        return null;
    }

    /**
     * 将JSON字符串转换成对象
     *
     * @param jsonStr JSON字符串
     * @param clazz   对象类型
     * @return 对象
     */
    public static <T> T jsonStrToBean(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON 字符串转对象异常", e);
        }
        return null;
    }

    /**
     * 将JSON字符串转换成List列表
     *
     * @param jsonStr JSON字符串
     * @param clazz   列表元素类型
     * @return List列表
     */
    public static <T> List<T> jsonStrToList(String jsonStr, Class<T> clazz) {
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return OBJECT_MAPPER.readValue(jsonStr, javaType);
        } catch (JsonProcessingException e) {
            log.error("JSON 字符串转 List 列表异常", e);
        }
        return null;
    }

    /**
     * 将JSON字符串转换成Set集合
     *
     * @param jsonStr JSON字符串
     * @param clazz   集合元素类型
     * @return Set集合
     */
    public static <E> Set<E> jsonStrToSet(String jsonStr, Class<E> clazz) {
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(Set.class, clazz);
            return OBJECT_MAPPER.readValue(jsonStr, javaType);
        } catch (JsonProcessingException e) {
            log.error("JSON 字符串转 Set 集合异常", e);
        }
        return null;
    }

    /**
     * 将JSON字符串转换成Map集合
     *
     * @param jsonStr   JSON字符串
     * @param keyType   键类型
     * @param valueType 值类型
     * @return Map集合
     */
    public static <K, V> Map<K, V> jsonStrToMap(String jsonStr, Class<K> keyType, Class<V> valueType) {
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);
            return OBJECT_MAPPER.readValue(jsonStr, javaType);
        } catch (JsonProcessingException e) {
            log.error("JSON 字符串转 Map 集合异常", e);
        }
        return null;
    }

    /**
     * 将JSON字符串转换成JsonNode
     *
     * @param jsonStr JSON字符串
     * @return JsonNode
     */
    public static JsonNode jsonStrToJsonNode(String jsonStr) {
        try {
            return OBJECT_MAPPER.readTree(jsonStr);    // OBJECT_MAPPER.readValue(jsonStr, JsonNode.class); 效果一样
        } catch (JsonProcessingException e) {
            log.error("JSON 字符串转 JsonNode 异常", e);
        }
        return null;
    }

    /**
     * 将对象转换成JsonNode
     *
     * @param object 对象
     * @return JsonNode
     */
    public static JsonNode beanToJsonNode(Object object) {
        try {
            return OBJECT_MAPPER.valueToTree(object);
        } catch (IllegalArgumentException e) {
            log.error("对象转 JsonNode 异常", e);
        }
        return null;
    }

    /**
     * 将JsonNode转换成对象
     *
     * @param jsonNode 对象
     * @return 对象
     */
    public static <T> T beanToJsonNode(JsonNode jsonNode, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.treeToValue(jsonNode, clazz);
        } catch (JsonProcessingException e) {
            log.error("JsonNode 转对象异常", e);
        }
        return null;
    }

}
