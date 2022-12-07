package com.vibee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableFeignClients
//@ComponentScan({ "com.vibee" })
public class VibeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VibeeApplication.class, args);
	}

}
