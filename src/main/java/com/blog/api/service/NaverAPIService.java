package com.blog.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NaverAPIService implements ExternalAPIService {
    
    @Getter
    private final String baseUrl;
    private final String clientId;
    private final String clientSecret;

    private final ObjectMapper objectMapper;

    public NaverAPIService(String baseUrl, String clientId, String clientSecret, ObjectMapper objectMapper) {
        this.baseUrl = baseUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean checkServer() {
        boolean result = false;

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(
                createHttpRequest(URLEncoder.encode("상태", "UTF-8"), EBlogSort.ACCURACY, 1, 1),
                HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == HttpStatus.OK.value()) result = true;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            log.warn("Fail to connect server", e);
        }

        return result;
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
