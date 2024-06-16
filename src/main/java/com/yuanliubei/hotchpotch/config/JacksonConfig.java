package com.yuanliubei.hotchpotch.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yuanliubei.hotchpotch.utils.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author yuanlb
 * @since 2022/10/25
 */
@Configuration
public class JacksonConfig {

    @Autowired
    private JacksonProperties jacksonProperties;


    /**
     * 自定义用于注入jackson的配置辅助接口
     * @return
     */
    @Bean
    @Order(1000)
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return (jacksonObjectMapperBuilder) -> {
            jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(dateTimeFormatter), new LocalDateSerializer(dateFormatter));
            jacksonObjectMapperBuilder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter), new LocalDateDeserializer(dateFormatter));
            jacksonObjectMapperBuilder.serializerByType(String.class, new JsonSerializer<String>() {
                public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                    jsonGenerator.writeString(StringUtils.trim(s));
                }
            });
            jacksonObjectMapperBuilder.deserializerByType(String.class, new JsonDeserializer<String>() {
                public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                    return StringUtils.trim(jsonParser.getText());
                }
            });
        };
    }

//    @Bean
//    @Primary
//    @ConditionalOnMissingBean(ObjectMapper.class)
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper mapper = builder.createXmlMapper(false).build();
//        //"" or null 都不序列化
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//        //反序列化时,遇到未知属性(那些没有对应的属性来映射的属性,并且没有任何setter或handler来处理这样的属性)时是否引起结果失败
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        /** 为objectMapper注册一个带有SerializerModifier的Factory */
//        mapper.setSerializerFactory(mapper.getSerializerFactory());
////                .withSerializerModifier(new MyBeanSerializerModifier()));
//
//        SerializerProvider serializerProvider = mapper.getSerializerProvider();
//        serializerProvider.setNullValueSerializer(
//                new CustomizeNullJsonSerializer.NullObjectJsonSerializer()
//        );
//
//        JacksonUtil.setSharedObjectMapper(mapper);
//        return mapper;
//    }


    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.build();

        // 日期格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        //GMT+8
        //map.put("CTT", "Asia/Shanghai");
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        // Include.NON_NULL 属性为NULL 不序列化
        //ALWAYS // 默认策略，任何情况都执行序列化
        //NON_EMPTY // null、集合数组等没有内容、空字符串等，都不会被序列化
        //NON_DEFAULT // 如果字段是默认值，就不会被序列化
        //NON_ABSENT // null的不会序列化，但如果类型是AtomicReference，依然会被序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //允许字段名没有引号（可以进一步减小json体积）：
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        //允许单引号：
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // 允许出现特殊字符和转义符
        //mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);这个已经过时。
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

        //允许C和C++样式注释：
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

        //如果对象申明的属性是基本类型，但是反序列化时传递为null，反序列化时会报错。
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        //序列化结果格式化，美化输出
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        //枚举输出成字符串
        //WRITE_ENUMS_USING_INDEX：输出索引
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        //空对象不要抛出异常：
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        //Date、Calendar等序列化为时间格式的字符串(如果不执行以下设置，就会序列化成时间戳格式)：
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //反序列化时，遇到未知属性不要抛出异常：
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        //反序列化时，遇到忽略属性不要抛出异常：
        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        //反序列化时，空字符串对于的实例属性为null：
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        JacksonUtil.setSharedObjectMapper(mapper);

        return mapper;
    }
}
