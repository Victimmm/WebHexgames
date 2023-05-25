package com.lxc.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer //@EurekaServer服务器端启动类，接受其他微服务注册进来
@SpringBootApplication
public class EurekaServer7001_APP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(EurekaServer7001_APP.class,args);

	}

}
