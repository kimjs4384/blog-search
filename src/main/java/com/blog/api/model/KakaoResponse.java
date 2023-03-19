package com.blog.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoResponse {
    
    private Meta meta;
    
    private List<Blog> documents;

    @Getter
    @Setter
    public static class Meta {
        @JsonAlias("is_end")
        private boolean isEnd;
        private float pageableCount;
        private float totalCount;
    }

}
