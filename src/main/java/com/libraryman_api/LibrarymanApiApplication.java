package com.libraryman_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
@EnableScheduling
@EnableCaching(proxyTargetClass = true)
public class LibrarymanApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibrarymanApiApplication.class, args);
    }

}
