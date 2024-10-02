package com.libraryman_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LibrarymanApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarymanApiApplication.class, args);
	}

}
