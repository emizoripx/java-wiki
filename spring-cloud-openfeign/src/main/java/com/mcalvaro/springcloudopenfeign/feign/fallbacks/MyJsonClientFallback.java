package com.mcalvaro.springcloudopenfeign.feign.fallbacks;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mcalvaro.springcloudopenfeign.dto.PostDto;
import com.mcalvaro.springcloudopenfeign.feign.IMyJsonClient;

@Component
public class MyJsonClientFallback implements IMyJsonClient {

    private final Logger logger = LoggerFactory.getLogger(MyJsonClientFallback.class);

    @Override
    public List<PostDto> getAllPosts() {

        // TODO: Habilitar hytrix
        logger.debug("Fallback Process...................");
        return Collections.emptyList();
    }

	@Override
	public PostDto getPost(int id) {
        return new PostDto(0, "UNKNOW");
	}

}
