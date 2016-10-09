package spring;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by xlo on 16/2/26.
 * It's the main class
 */
@SpringBootApplication()
public class Main {

    public static void main(String[] args) {
        PropertyConfigurator.configure("./log4j.properties");
        SpringApplication.run(Main.class, args);
    }

}
