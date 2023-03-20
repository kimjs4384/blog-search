package com.blog.api.service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.blog.api.exceptions.APIServerException;
import com.blog.api.model.dto.BlogListResponse;
import com.blog.api.model.enums.EBlogSort;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BlogService {

    private final ExternalAPIService externalAPIService;
    private final KeywordService keywordService;

    public BlogService(ExternalAPIService externalAPIService, KeywordService keywordService) {
        this.externalAPIService = externalAPIService;
        this.keywordService = keywordService;
    }

    public BlogListResponse retrieveBlogs(String keyword, EBlogSort sort, int page, int size) {
        HttpClient client = HttpClient.newHttpClient();
        
        BlogListResponse blogListResponse = null;
        try {
            HttpResponse<String> response = client.send(
                externalAPIService.createHttpRequest(keyword, sort, page, size),
                HttpResponse.BodyHandlers.ofString()
            );
            int statusCode = response.statusCode();
            if (statusCode == HttpStatus.OK.value()) {
                blogListResponse = externalAPIService.parseResponse(response.body());
                if (page == 50) blogListResponse.setHasNext(false);
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value() | statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                throw new APIServerException(String.format("API Server(%s) is not working.", externalAPIService.getBaseUrl()));
            } else if (statusCode >= 400 && statusCode <= 499) {

            }
        } catch (ConnectException e) {
            log.warn("API server internal error.", e);
            throw new APIServerException(String.format("Fail to connect API Server(%s)", externalAPIService.getBaseUrl()));
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

        return blogListResponse;
    }

}
