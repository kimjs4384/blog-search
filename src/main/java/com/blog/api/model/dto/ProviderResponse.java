package com.blog.api.model.dto;

import java.time.LocalDateTime;

import com.blog.api.model.enums.EAPIProvider;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class ProviderResponse {

    private EAPIProvider currentProvider;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime timestamp;

    public ProviderResponse() {}

    public ProviderResponse(EAPIProvider currentProvider) {
        this.currentProvider = currentProvider;
        timestamp = LocalDateTime.now();
    }
    
}
