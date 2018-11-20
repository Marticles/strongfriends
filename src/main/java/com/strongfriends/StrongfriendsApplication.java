package com.strongfriends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StrongfriendsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrongfriendsApplication.class, args);
	}
}
