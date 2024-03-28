package com.teamqr.springcloudconfigdemoclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RefreshScope
public class SpringCloudConfigDemoClientApplication {

	@Value("${my.var1:default1}")
	private String value1;

	@Value("${var2:default2}")
	private String value2;

	@RequestMapping("/view")
	public String pruebaValues() {
		return String.format("Value 1 es \"%s\" y Value 2 es \"%s\"", value1, value2 );
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudConfigDemoClientApplication.class, args);
	}

}
