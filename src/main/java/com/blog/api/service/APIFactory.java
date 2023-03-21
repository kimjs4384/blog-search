package com.blog.api.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.blog.api.model.enums.EAPIProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class APIFactory {
    
    private final Environment environment;
    private final ObjectMapper objectMapper;

    public APIFactory(Environment environment, ObjectMapper objectMapper) {
        this.environment = environment;
        this.objectMapper = objectMapper;
    }

    public ExternalAPIService getAPIService(EAPIProvider provider) {
        ExternalAPIService apiService = null;
        if (provider.equals(EAPIProvider.KAKAO)) {
            apiService = createKaKaoService();
        } else if (provider.equals(EAPIProvider.NAVER)) {
            apiService = createNaverService();
        }

        return apiService;
    };

    private ExternalAPIService createKaKaoService() {
        String baseUrl = environment.getProperty("api.kakao.base-url", "https://dapi.kakao.com/v2/search/blog");
        String authKey = environment.getProperty("api.kakao.auth-key", "KakaoAK 6c2966d258c846c86b92773504c4136d");

        ExternalAPIService service = new KakaoAPIService(baseUrl, authKey, objectMapper);
        return service.checkServer() ? service : null;
    }

    private ExternalAPIService createNaverService() {
        String baseUrl = environment.getProperty("api.naver.base-url", "https://openapi.naver.com/v1/search/blog.json");
        String clientId = environment.getProperty("api.naver.client-id", "193aAEonfwwuYc1BoYdq");
        String clientSecret = environment.getProperty("api.naver.client-secret", "Y9jCSvH1Wf");
    
        ExternalAPIService service = new NaverAPIService(baseUrl, clientId, clientSecret, objectMapper);
        return service.checkServer() ? service : null;
    }

}
