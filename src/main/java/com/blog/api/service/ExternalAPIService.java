package com.blog.api.service;

import java.net.URISyntaxException;
import java.net.http.HttpRequest;

import com.blog.api.model.dto.BlogListResponse;
import com.blog.api.model.enums.EBlogSort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface ExternalAPIService {

    String getBaseUrl();

    boolean checkServer();

    HttpRequest createHttpRequest(String keyword, EBlogSort sort, int page, int size)
        throws URISyntaxException ;

    BlogListResponse parseResponse(String response)
        throws JsonMappingException, JsonProcessingException;
    
}
