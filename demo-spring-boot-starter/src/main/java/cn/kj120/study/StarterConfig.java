package cn.kj120.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// application.properties 配置文件中存在 demoStarter 配置才自动注入
@ConditionalOnProperty(value = "potato.name", havingValue = "potato")
@EnableConfigurationProperties(UserConfig.class)
@ConditionalOnClass(UserConfig.class)
public class StarterConfig {

    @Autowired
    private UserConfig userConfig;

    @Bean
    public DemoService demoService() {
        return new DemoService(userConfig);
    }
}
