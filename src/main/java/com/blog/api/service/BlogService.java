package com.blog.api.service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.blog.api.exceptions.APIServerException;
import com.blog.api.model.Blog;
import com.blog.api.model.BlogListResponse;
import com.blog.api.model.EBlogSort;
import com.blog.api.model.KakaoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BlogService {

    @Value("${api.kakao.base-url}")
    private String baseUrl;
    @Value("${api.kakao.auth-key}")
    private String authKey;

    private final ObjectMapper objectMapper;
    private final KeywordService keywordService;

    public BlogService(ObjectMapper objectMapper, KeywordService keywordService) {
        this.objectMapper = objectMapper;
        this.keywordService = keywordService;
    }

    public BlogListResponse retrieveBlogs(String keyword, EBlogSort sort, int page, int size) {
        HttpClient client = HttpClient.newHttpClient();
        String queryString = String.format("?query=%s&sort=%s&page=%d&size=%d", keyword, sort.getValue(), page, size);
        HttpResponse<String> response;
        
        List<Blog> resultList = new ArrayList<>();
        boolean hasNext = true;
        try {
            response = client.send(
                HttpRequest.newBuilder(new URI(baseUrl + queryString)).setHeader(HttpHeaders.AUTHORIZATION, authKey).build(),
                HttpResponse.BodyHandlers.ofString()
            );
            int statusCode = response.statusCode();
            if (statusCode == HttpStatus.OK.value()) {
                KakaoResponse kakaoResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});
                
                resultList.addAll(kakaoResponse.getDocuments());
                if (kakaoResponse.getMeta().isEnd() || page == 50) hasNext = false; 
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value() | statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                throw new APIServerException(String.format("API Server(%s) is not working.", baseUrl));
            } else if (statusCode >= 400 && statusCode <= 499) {

            }
        } catch (ConnectException e) {
            log.warn("API server internal error.", e);
            throw new APIServerException(String.format("Fail to connect API Server(%s)", baseUrl));
        } catch (JsonProcessingException e) {
            // 응답 파싱 오류 
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            // send() 오휴
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TOOD api base url 설정 오류 
            e.printStackTrace();
        }

        keywordService.countKeyword(keyword);

        return new BlogListResponse(resultList, hasNext);
    }

}
