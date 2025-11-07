package com.fil.pickple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PickpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(PickpleApplication.class, args);
	}

}
