package com.mcalvaro.springcloudopenfeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.mcalvaro.springcloudopenfeign.feign.config.RequestCatcherClientConfiguration;

@FeignClient(value = "RequestCatcherClient", url = "https://alvaroez.requestcatcher.com", configuration = RequestCatcherClientConfiguration.class)
public interface IRequestCatcherClient {
 
    @PostMapping("/demo")
    Void requestDemo();
}
