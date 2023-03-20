package com.blog.api.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordResponse {
    
    private List<KeywordDTO> keywords;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime timestamp;

    public KeywordResponse() {}

    public KeywordResponse(List<KeywordDTO> keywords) {
        this.keywords = keywords;
        this.timestamp = LocalDateTime.now();
    }

}
