package com.blog.api.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime timestamp;
    private String message;
    private List<String> details;

    public ExceptionResponse() {}

    public ExceptionResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }

    public ExceptionResponse(String message, List<String> details) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.details = details;
    }

}
