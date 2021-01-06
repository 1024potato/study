package cn.kj120.study.expiration.spring.cache.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "cache")
public class Cache {
    /**
     * 所有缓存的前缀
     */
    @Value("${cache.prefix:}")
    private String prefix;

    /**
     * 缓存默认有效期单位秒
     */
    @Value("${cache.defaultTtl:365d}")
    private String defaultTtl;

    /**
     * 缓存key名和有效期分隔符
     */
    @Value("${cache.delimiter:#}")
    private String delimiter;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDefaultTtl() {
        return defaultTtl;
    }

    public void setDefaultTtl(String defaultTtl) {
        this.defaultTtl = defaultTtl;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
