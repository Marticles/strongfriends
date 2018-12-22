package com.strongfriends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StrongfriendsApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(StrongfriendsApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(StrongfriendsApplication.class, args);
	}
}




//@SpringBootApplication
//@EnableScheduling
//public class StrongfriendsApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(StrongfriendsApplication.class, args);
//	}
//}