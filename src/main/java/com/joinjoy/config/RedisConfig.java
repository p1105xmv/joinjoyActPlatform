package com.joinjoy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 設置key序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // 設置value序列化方式
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 設置hash key序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // 設置hash value序列化方式
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
