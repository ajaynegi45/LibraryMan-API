package com.libraryman_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = {"com.libraryman_api"})
@EnableAsync
public class LibrarymanApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarymanApiApplication.class, args);
	}

}
