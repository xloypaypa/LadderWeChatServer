package spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by xsu on 16/10/11.
 * it's the loader
 */
@Configuration
@PropertySource(value = "file:./ladder.properties")
public class LadderConfigLoader {

    @Bean
    public LadderConfig ladderConfig() {
        return new LadderConfig();
    }

}
