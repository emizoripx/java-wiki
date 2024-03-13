package com.mcalvaro.springcloudopenfeign.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mcalvaro.springcloudopenfeign.dto.PostDto;
import com.mcalvaro.springcloudopenfeign.feign.config.MyJsonClientConfiguration;
import com.mcalvaro.springcloudopenfeign.feign.fallbacks.MyJsonClientFallback;

@FeignClient(value = "JsonClient", url = "https://my-json-server.typicode.com", configuration = MyJsonClientConfiguration.class, fallback = MyJsonClientFallback.class)
public interface IMyJsonClient {
 
    @GetMapping("typicode/demo/posts")
    List<PostDto> getAllPosts();

    @GetMapping("typicode/demo/posts/{id}")
    PostDto getPost(@PathVariable int id);
}
