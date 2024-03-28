package com.teamqr.springcloudconfigdemoserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigServer
@RestController
public class SpringCloudConfigDemoServerApplication {

	@Value("${meessage:otro}")
	private String message;

	@GetMapping("message")
	public String mensaje(){
		return message;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudConfigDemoServerApplication.class, args);
	}

}
