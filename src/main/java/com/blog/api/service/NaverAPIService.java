package com.blog.api.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import com.blog.api.model.Blog;
import com.blog.api.model.dto.BlogListResponse;
import com.blog.api.model.dto.NaverResponse;
import com.blog.api.model.dto.NaverResponse.Item;
import com.blog.api.model.enums.EBlogSort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Service
@ConditionalOnMissingBean(name =  "kakaoAPIService")
public class NaverAPIService implements ExternalAPIService {

    @Getter
    @Value("${api.naver.base-url}")
    private String baseUrl;
    @Value("${api.naver.client-id}")
    private String clientId;
    @Value("${api.naver.client-secret}")
    private String clientSecret;

    private final ObjectMapper objectMapper;

    public NaverAPIService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean checkServer() {
        return true;
    }

    @Override
    public HttpRequest createHttpRequest(String keyword, EBlogSort sort, int page, int size) throws URISyntaxException {
        String sortOption = sort.equals(EBlogSort.ACCURACY) ? "sim" : "date";
        String queryString = String.format("?query=%s&sort=%s&start=%d&display=%d", keyword, sortOption, page, size);
        HttpRequest httpRequest = HttpRequest.newBuilder(new URI(baseUrl + queryString))
            .setHeader("X-Naver-Client-Id", clientId)
            .setHeader("X-Naver-Client-Secret", clientSecret)
            .build();
        return httpRequest;
    }

    @Override
    public BlogListResponse parseResponse(String response) throws JsonMappingException, JsonProcessingException {
        NaverResponse naverResponse = objectMapper.readValue(response, new TypeReference<>() {});
        List<Item> items = naverResponse.getItems();
        List<Blog> blogs = items.stream().map(item -> item.toBlog()).collect(Collectors.toList());
        return new BlogListResponse(blogs, true);
    }
}
