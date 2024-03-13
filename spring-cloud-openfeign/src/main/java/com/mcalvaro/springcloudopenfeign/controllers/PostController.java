package com.mcalvaro.springcloudopenfeign.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mcalvaro.springcloudopenfeign.dto.PostDto;
import com.mcalvaro.springcloudopenfeign.feign.IMyJsonClient;
import com.mcalvaro.springcloudopenfeign.feign.IRequestCatcherClient;

@RestController
public class PostController {

    private IMyJsonClient myJsonClient;

    private IRequestCatcherClient requestCatcherClient;

    public PostController(IMyJsonClient myJsonClient, IRequestCatcherClient requestCatcherClient) {
        this.myJsonClient = myJsonClient;
        this.requestCatcherClient = requestCatcherClient;
    }
 
    @GetMapping("posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {

        return ResponseEntity.ok(myJsonClient.getAllPosts());
    }

    @GetMapping("posts/{id}")
    public ResponseEntity<PostDto> getPost( @PathVariable int id ) {

        PostDto post = myJsonClient.getPost(id);

        return ResponseEntity.ok(post);
    }

    @GetMapping("interceptors")
    public ResponseEntity<Void> testInterceptor() {

        requestCatcherClient.requestDemo();
        return ResponseEntity.ok().build();
    }
}
