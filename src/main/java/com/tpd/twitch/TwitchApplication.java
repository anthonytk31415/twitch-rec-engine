package com.tpd.twitch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication      // Even if there is a psvm below, I definitely need this annotation. This annotation tells Spring to perform an initialization operation when it finds it during runtime.
@EnableFeignClients
@EnableCaching              // Tell Spring Boot that the entire project needs to enable caching.
public class TwitchApplication {
    // All Java applications need this. It calls functions from here to start the program.
    // But Spring doesn't run because this main function is called; it's because the
    // annotation above tells Spring that it should be called.
    public static void main(String[] args) {
        SpringApplication.run(TwitchApplication.class, args);
    }

}
