package cn.kj120.study.expiration.spring.cache.starter;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存配置
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(Cache.class)
public class CacheConfig {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Cache cache;

    /**
     * 过期时间单位对应秒转换倍数 m -> 60  h-> 3600  d -> 86400
     */
    private Map<String, Long> ttlUnit = new HashMap<>(4);

    @PostConstruct
    public void init() {
        ttlUnit.put("s", 1L);
        ttlUnit.put("m", 60L);
        ttlUnit.put("h", 3600L);
        ttlUnit.put("d", 86400L);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, Jackson2JsonRedisSerializer redisCacheSerializer){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // 设置默认有效期
                .entryTtl(Duration.ofSeconds(ttlSecond(cache.getDefaultTtl())))
                // 设置缓存key前缀
                .computePrefixWith(cacheName -> cache.getPrefix() + cacheName)
                // 设置存储格式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisCacheSerializer));

        MyRedisCacheManager myRedisCacheManager = new MyRedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), redisCacheConfiguration);
        return myRedisCacheManager;
    }

    @Bean
    public Jackson2JsonRedisSerializer redisCacheSerializer() {
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 序列化的json字符串里面包含包信息
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);

        redisSerializer.setObjectMapper(objectMapper);
        return redisSerializer;
    }


    private class MyRedisCacheManager extends RedisCacheManager {

        public MyRedisCacheManager(RedisCacheWriter redisCacheWriter, RedisCacheConfiguration redisCacheConfiguration){
            super(redisCacheWriter, redisCacheConfiguration);
        }

        @Override
        protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
            log.info("缓存的key: {} ", name);
            String[] redisKeyArray = StringUtils.delimitedListToStringArray(name, cache.getDelimiter());
            name = redisKeyArray[0] + ":";
            // 设置过期时间单位秒
            if (redisKeyArray.length > 1){
                long ttl = ttlSecond(redisKeyArray[1]);
                cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
            }
            return super.createRedisCache(name, cacheConfig);
        }
    }

    /**
     * 单位转换成秒数
     * @param ttlString
     * @return
     */
    private Long ttlSecond(String ttlString) {
        // 获取单位
        String unit = ttlString.substring(ttlString.length() - 1);
        // 获取单位对应的秒倍数
        Long multiple = ttlUnit.get(unit);
        if (multiple == null) {
            multiple = 1L;
        } else {
            ttlString = ttlString.substring(0, ttlString.length() - 1);
        }
        return Long.parseLong(ttlString) * multiple;
    }

}
