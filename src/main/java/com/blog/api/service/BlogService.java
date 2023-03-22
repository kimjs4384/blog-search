package com.blog.api.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.blog.api.exceptions.APIServerException;
import com.blog.api.model.dto.BlogListResponse;
import com.blog.api.model.enums.EAPIProvider;
import com.blog.api.model.enums.EBlogSort;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BlogService {

    @Getter
    private EAPIProvider currentAPIProvider = EAPIProvider.KAKAO;

    private final APIFactory apiFactory;
    private final KeywordService keywordService;

    private ExternalAPIService externalAPIService;

    public BlogService(APIFactory apiFactory, KeywordService keywordService) {
        this.apiFactory = apiFactory;
        this.keywordService = keywordService;
    }

    public BlogListResponse retrieveBlogs(String keyword, EBlogSort sort, int page, int size) {
        if (externalAPIService == null) setExternalAPIService();

        HttpClient client = HttpClient.newHttpClient();
        
        BlogListResponse blogListResponse = null;
        try {
            HttpResponse<String> response = client.send(
                externalAPIService.createHttpRequest(URLEncoder.encode(keyword, "UTF-8"), sort, page, size),
                HttpResponse.BodyHandlers.ofString()
            );
            int statusCode = response.statusCode();
            if (statusCode == HttpStatus.OK.value()) {
                blogListResponse = externalAPIService.parseResponse(response.body());
                if (page == 50) blogListResponse.setHasNext(false);

                keywordService.asyncCountKeyword(keyword);
            } else {
                externalAPIService = null;
                retrieveBlogs(keyword, sort, page, size);
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            log.warn("", e);
            externalAPIService = null;
            retrieveBlogs(keyword, sort, page, size);
        }

        return blogListResponse;
    }

    private void setExternalAPIService() {
        for (EAPIProvider provider: EAPIProvider.values()) {
            externalAPIService = apiFactory.getAPIService(provider);
            if (externalAPIService != null) {
                currentAPIProvider = provider;
                break;
            }
        }

        if (externalAPIService == null) {
            throw new APIServerException("All api server is not working.");
        }
    }
}
