package spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by xsu on 16/10/8.
 * it's the token config
 */
@Configuration
@PropertySource(value = "file:./WeChat.properties")
public class WeChatServerConfigLoader {

    @Bean
    public ServerConfig serverConfig() {
        return new ServerConfig();
    }

}
