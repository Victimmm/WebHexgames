package com.lwm.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.lwm.springcloud.dao")
public class LoginApplication_8001 {

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication_8001.class, args);

	}

}
