package com.nnit.pb.common.utils;

//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.JsonParser;
//import org.codehaus.jackson.map.DeserializationConfig;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
//import com.zhu.dms.pojo.Person;


public class JsonUtil {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
//        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
//        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
//        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
//        objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
//        objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES, true);
//        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	// 转换为格式化的json
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //修改日期格式
    	objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static String encode(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonGenerationException e) {
        	e.printStackTrace();
            //logger.error("encode(Object)", e); //$NON-NLS-1$
        } catch (JsonMappingException e) {
        	e.printStackTrace();
            //logger.error("encode(Object)", e); //$NON-NLS-1$
        } catch (IOException e) {
        	e.printStackTrace();
            //logger.error("encode(Object)", e); //$NON-NLS-1$
        } catch (Exception e) {
        	e.printStackTrace();
            //logger.error("decode(String, JsonTypeReference<T>)", e);
        }
        return null;
    }

    /**
     * 将json string反序列化成对象
     *
     * @param json
     * @param valueType
     * @return
     */
    public static <T> T decode(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonParseException e) {
        	e.printStackTrace();
            //logger.error("decode(String, Class<T>)", e);
        } catch (JsonMappingException e) {
        	e.printStackTrace();
            //logger.error("decode(String, Class<T>)", e);
        } catch (IOException e) {
        	e.printStackTrace();
            //logger.error("decode(String, Class<T>)", e);
        } catch (Exception e) {
        	e.printStackTrace();
            //logger.error("decode(String, JsonTypeReference<T>)", e);
        }
        return null;
    }

    /**
     * 将json array反序列化为对象
     *
     * @param json
     * @param jsonTypeReference
     * @return
     */
    public static <T> T decode(String json, TypeReference<T> jsonTypeReference) {
        try {
            return (T) objectMapper.readValue(json, jsonTypeReference);
        } catch (JsonParseException e) {
        	e.printStackTrace();
            //logger.error("decode(String, JsonTypeReference<T>)", e);
        } catch (JsonMappingException e) {
        	e.printStackTrace();
            //logger.error("decode(String, JsonTypeReference<T>)", e);
        } catch (IOException e) {
        	e.printStackTrace();
            //logger.error("decode(String, JsonTypeReference<T>)", e);
        } catch (Exception e) {
        	e.printStackTrace();
            //logger.error("decode(String, JsonTypeReference<T>)", e);
        }
        return null;
    }
    
    public static void main(String[] args) {
//    	Person person = new Person();
//        person.setName("test");
//        person.setAge(11);
//        person.setAddress("addr");
//        person.setHobby("hobby");
//        String jsonStr = JsonUtil.encode(person);
//        logger.debug(jsonStr);
//        
//        Person newPerson = JsonUtil.decode(jsonStr, new TypeReference<Person>() {});
//        logger.debug(newPerson.getName());
//        Person newPerson2 = JsonUtil.decode(jsonStr, Person.class);
//        logger.debug(newPerson2.getHobby());
//        
//        List list = new ArrayList();
//        Person person1 = new Person();
//        person1.setName("test");
//        person1.setAge(11);
//        person1.setAddress("addr");
//        person1.setHobby("hobby");
//        Person person2 = new Person();
//        person2.setName("test2");
//        person2.setAge(112);
//        person2.setAddress("addr2");
//        person2.setHobby("hobby2");
//        list.add(person1);
//        list.add(person2);
//        String jsonListStr = JsonUtil.encode(list);
//        logger.debug(jsonListStr);
//        
//        List<Person> scriptList = JsonUtil.decode(jsonListStr, new TypeReference<List<Person>>() {});
//        logger.debug(scriptList.toString());
//        logger.debug(scriptList.get(1).getHobby());
        
    }
    
    
    
    
}