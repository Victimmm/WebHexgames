package com.lwm.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan("com.lwm.springcloud.dao")
@EnableEurekaClient
public class websocket9001_app {
	public static void main(String[] args) {
		SpringApplication.run(websocket9001_app.class, args);
	}
} 

//mvn install:install-file  -Dfile=D:\hex-api.jar -DgroupId=com.lwm -DartifactId=hex-api -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar