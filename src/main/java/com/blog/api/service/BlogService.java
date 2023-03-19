package com.blog.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.blog.api.model.Blog;
import com.blog.api.model.EBlogSort;
import com.blog.api.model.KakaoResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BlogService {

    @Value("${api.kakao.base-url}")
    private String baseUrl;
    @Value("${api.kakao.auth-key}")
    private String authKey;

    private final ObjectMapper objectMapper;

    public BlogService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Blog> retrieveBlogs(String keyword, EBlogSort sort, int page, int size) 
        throws IOException, InterruptedException, URISyntaxException 
    {
        HttpClient client = HttpClient.newHttpClient();
        String queryString = String.format("?query=%s&sort=%s&page=%d&size=%d", keyword, sort.getValue(), page, size);
        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(baseUrl + queryString)).setHeader(HttpHeaders.AUTHORIZATION, authKey).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        KakaoResponse kakaoResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});
        return kakaoResponse.getDocuments();
    }

}
