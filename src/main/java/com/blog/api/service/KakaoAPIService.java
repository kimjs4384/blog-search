package com.blog.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.blog.api.model.dto.BlogListResponse;
import com.blog.api.model.dto.KakaoResponse;
import com.blog.api.model.enums.EBlogSort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KakaoAPIService implements ExternalAPIService {

    @Getter
    private final String baseUrl;
    private final String authKey;

    private final ObjectMapper objectMapper;

    public KakaoAPIService(String baseUrl, String authKey, ObjectMapper objectMapper) {
        this.baseUrl = baseUrl;
        this.authKey = authKey;
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
