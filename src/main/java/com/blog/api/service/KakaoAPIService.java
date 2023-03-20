package com.blog.api.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.blog.api.model.dto.BlogListResponse;
import com.blog.api.model.dto.KakaoResponse;
import com.blog.api.model.enums.EBlogSort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Service
public class KakaoAPIService implements ExternalAPIService {

    @Getter
    @Value("${api.kakao.base-url}")
    private String baseUrl;
    @Value("${api.kakao.auth-key}")
    private String authKey;

    private final ObjectMapper objectMapper;

    public KakaoAPIService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // TODO
    @Override
    public boolean checkServer() {
        return true;
    }

    @Override
    public HttpRequest createHttpRequest(String keyword, EBlogSort sort, int page, int size) throws URISyntaxException {
        String queryString = String.format("?query=%s&sort=%s&page=%d&size=%d", keyword, sort.getValue(), page, size);
        HttpRequest httpRequest = HttpRequest.newBuilder(new URI(baseUrl + queryString)).setHeader(HttpHeaders.AUTHORIZATION, authKey).build();
        return httpRequest;
    }

    @Override
    public BlogListResponse parseResponse(String response) throws JsonMappingException, JsonProcessingException {
        KakaoResponse kakaoResponse = objectMapper.readValue(response, new TypeReference<>() {});
        return new BlogListResponse(kakaoResponse.getDocuments(), !kakaoResponse.getMeta().isEnd()) ;
    }
    
}
