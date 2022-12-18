package com.vibee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableFeignClients
@EnableScheduling
//@ComponentScan({ "com.vibee" })
public class VibeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VibeeApplication.class, args);
	}

}
