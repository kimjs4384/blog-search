package com.blog.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.blog.api.model.dto.BlogListResponse;
import com.blog.api.model.dto.ExceptionResponse;
import com.blog.api.model.dto.ProviderResponse;
import com.blog.api.model.enums.EAPIProvider;
import com.blog.api.model.enums.EBlogSort;
import com.blog.api.repository.KeywordJPARepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-blog")
public class BlogRestControllerTests {

    @LocalServerPort
    int port;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    KeywordJPARepository keywordRepository;

    /**
     * 블로그 keyword 검색
     * status code : 200
     */
    @Test
    void testRetrieveBlogs() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "타입스크립트";
        String url = String.format("http://localhost:%d/blogs?keyword=%s", this.port, keyword);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        BlogListResponse blogListResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertThat(blogListResponse.getBlogs().size()).isGreaterThan(1);
        assertThat(blogListResponse.getBlogs().size()).isLessThanOrEqualTo(10);
        assertThat(keywordRepository.findByKeyword(keyword).isPresent()).isEqualTo(true);
    }

    /**
     * 블로그 keyword 검색 - sort 조건 추가 
     * status code : 200
     */
    @Test
    void testRetrieveBlogsWithSort() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "타입스크립트";
        EBlogSort sort = EBlogSort.RECENCY;

        HttpClient client = HttpClient.newHttpClient();

        String url = String.format("http://localhost:%d/blogs?keyword=%s", this.port, keyword);

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        BlogListResponse blogListResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});
        assertThat(blogListResponse.getBlogs().size()).isNotEqualTo(0);

        String urlWithSort = String.format("http://localhost:%d/blogs?keyword=%s&sort=%s", this.port, keyword, sort);

        HttpResponse<String> responseWithSort = client.send(
            HttpRequest.newBuilder(new URI(urlWithSort)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(responseWithSort.statusCode()).isEqualTo(HttpStatus.OK.value());
        BlogListResponse blogListResponseWithSort = objectMapper.readValue(responseWithSort.body(), new TypeReference<>() {});
        assertThat(blogListResponseWithSort.getBlogs().size()).isNotEqualTo(0);

        assertThat(blogListResponse.getBlogs().get(0).getTitle()).isNotEqualTo(blogListResponseWithSort.getBlogs().get(0).getTitle());
    }
     
    /**
     * 블로그 keyword 검색 - page 조건 추가 
     * status code : 200
     */
    @Test
    void testRetrieveBlogsWithPage() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "타입스크립트";
        int page = 2;

        HttpClient client = HttpClient.newHttpClient();

        String url = String.format("http://localhost:%d/blogs?keyword=%s", this.port, keyword);

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        BlogListResponse blogListResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});
        assertThat(blogListResponse.getBlogs().size()).isNotEqualTo(0);

        String urlWithPage = String.format("http://localhost:%d/blogs?keyword=%s&page=%d", this.port, keyword, page);

        HttpResponse<String> responseWithPage = client.send(
            HttpRequest.newBuilder(new URI(urlWithPage)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(responseWithPage.statusCode()).isEqualTo(HttpStatus.OK.value());
        BlogListResponse blogListResponseWithPage = objectMapper.readValue(responseWithPage.body(), new TypeReference<>() {});
        assertThat(blogListResponseWithPage.getBlogs().size()).isNotEqualTo(0);

        assertThat(blogListResponse.getBlogs().get(0).getTitle()).isNotEqualTo(blogListResponseWithPage.getBlogs().get(0).getTitle());
    }

    /**
     * 블로그 keyword 검색 - size 조건 추가 
     * status code : 200
     */
    @Test
    void testRetrieveBlogsWithSize() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "타입스크립트";
        int size = 20;

        HttpClient client = HttpClient.newHttpClient();

        String url = String.format("http://localhost:%d/blogs?keyword=%s", this.port, keyword);

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        BlogListResponse blogListResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});
        assertThat(blogListResponse.getBlogs().size()).isEqualTo(10);

        String urlWithSize = String.format("http://localhost:%d/blogs?keyword=%s&size=%d", this.port, keyword, size);

        HttpResponse<String> responseWithSize = client.send(
            HttpRequest.newBuilder(new URI(urlWithSize)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(responseWithSize.statusCode()).isEqualTo(HttpStatus.OK.value());
        BlogListResponse blogListResponseWithSize = objectMapper.readValue(responseWithSize.body(), new TypeReference<>() {});
        assertThat(blogListResponseWithSize.getBlogs().size()).isEqualTo(20);

        assertThat(blogListResponse.getBlogs().get(0).getTitle()).isEqualTo(blogListResponseWithSize.getBlogs().get(0).getTitle());
    }

    /**
     * 블로그 keyword 검색 - sort, page, size 요청 
     * status code : 200
     */
    @Test
    void testRetrieveBlogsWithAllOptions() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "타입스크립트";
        EBlogSort sort = EBlogSort.RECENCY;
        int page = 2;
        int size = 20;

        HttpClient client = HttpClient.newHttpClient();

        String url = String.format("http://localhost:%d/blogs?keyword=%s", this.port, keyword);

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        BlogListResponse blogListResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});
        assertThat(blogListResponse.getBlogs().size()).isEqualTo(10);

        String urlWithAll = String.format("http://localhost:%d/blogs?keyword=%s&sort=%s&page=%d&size=%d", this.port, keyword, sort, page, size);

        HttpResponse<String> responseWithAll = client.send(
            HttpRequest.newBuilder(new URI(urlWithAll)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(responseWithAll.statusCode()).isEqualTo(HttpStatus.OK.value());
        BlogListResponse blogListResponseWithAll = objectMapper.readValue(responseWithAll.body(), new TypeReference<>() {});
        assertThat(blogListResponseWithAll.getBlogs().size()).isEqualTo(20);

        assertThat(blogListResponseWithAll.getBlogs().get(0).getTitle()).isEqualTo(blogListResponseWithAll.getBlogs().get(0).getTitle());
    }
    
    /**
     * 블로그 keyword 검색 - keyword 누락
     * status code : 400
     */
    @Test
    void testExceptionRetrieveBlogsNoKeyword() throws IOException, InterruptedException, URISyntaxException {
        String url = String.format("http://localhost:%d/blogs", this.port);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertThat(exceptionResponse.getMessage()).isEqualTo("Requested param is not valid.");
        assertThat(exceptionResponse.getDetails().size()).isEqualTo(1);
        assertThat(exceptionResponse.getDetails().get(0)).contains("keyword is required option");
    }
    
    /**
     * 블로그 keyword 검색 - 공백 keyword
     * status code : 400
     */
    @Test
    void testExceptionRetrieveBlogsBlankKeyword() throws IOException, InterruptedException, URISyntaxException {
        String url = String.format("http://localhost:%d/blogs?keyword=", this.port);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertThat(exceptionResponse.getMessage()).isEqualTo("Requested param is not valid.");
        assertThat(exceptionResponse.getDetails().size()).isEqualTo(1);
        assertThat(exceptionResponse.getDetails().get(0)).contains("keyword is required option");
    }

    /**
     * 블로그 keyword 검색 - 지원하지 않는 sort 요청
     * status code : 400
     */
    @Test
    void testExceptionRetrieveBlogsNotSupportedSort() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "스프링부트";
        String sort = "ASC";
        String url = String.format("http://localhost:%d/blogs?keyword=%s&sort=%s", this.port, keyword, sort);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertThat(exceptionResponse.getMessage()).isEqualTo("Requested param is not valid.");
        assertThat(exceptionResponse.getDetails().size()).isEqualTo(1);
        assertThat(exceptionResponse.getDetails().get(0)).contains("Suport sort options");
    }

    /**
     * 블로그 keyword 검색 - page=0 요청
     * status code : 400
     */
    @Test
    void testExceptionRetrieveBlogsMinPage() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "스프링부트";
        int page = 0;
        String url = String.format("http://localhost:%d/blogs?keyword=%s&page=%s", this.port, keyword, page);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertThat(exceptionResponse.getMessage()).isEqualTo("Requested param is not valid.");
        assertThat(exceptionResponse.getDetails().size()).isEqualTo(1);
        assertThat(exceptionResponse.getDetails().get(0)).contains("Minimum page is 1");
    }
    
    /**
     * 블로그 keyword 검색 - page=100 요청
     * status code : 400
     */
    @Test
    void testExceptionRetrieveBlogsMaxPage() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "스프링부트";
        int page = 100;
        String url = String.format("http://localhost:%d/blogs?keyword=%s&page=%s", this.port, keyword, page);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertThat(exceptionResponse.getMessage()).isEqualTo("Requested param is not valid.");
        assertThat(exceptionResponse.getDetails().size()).isEqualTo(1);
        assertThat(exceptionResponse.getDetails().get(0)).contains("Maximum page is 50");
    }

    /**
     * 블로그 keyword 검색 - size=0 요청
     * status code : 400
     */
    @Test
    void testExceptionRetrieveBlogsMinSize() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "스프링부트";
        int size = 0;
        String url = String.format("http://localhost:%d/blogs?keyword=%s&size=%s", this.port, keyword, size);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertThat(exceptionResponse.getMessage()).isEqualTo("Requested param is not valid.");
        assertThat(exceptionResponse.getDetails().size()).isEqualTo(1);
        assertThat(exceptionResponse.getDetails().get(0)).contains("Minimum size is 1");
    }
    
    /**
     * 블로그 keyword 검색 - size=100 요청 
     * status code : 400
     */
    @Test
    void testExceptionRetrieveBlogsMaxSize() throws IOException, InterruptedException, URISyntaxException {
        String keyword = "스프링부트";
        int size = 100;
        String url = String.format("http://localhost:%d/blogs?keyword=%s&size=%s", this.port, keyword, size);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertThat(exceptionResponse.getMessage()).isEqualTo("Requested param is not valid.");
        assertThat(exceptionResponse.getDetails().size()).isEqualTo(1);
        assertThat(exceptionResponse.getDetails().get(0)).contains("Maximum size is 50");
    }

    /**
     * API 제공자 조회
     * status code : 200
     * @throws URISyntaxException
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    void testGetCurrentProvider() throws IOException, InterruptedException, URISyntaxException {
        String url = String.format("http://localhost:%d/blogs/provider", this.port);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ProviderResponse providerResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertThat(providerResponse.getCurrentProvider()).isIn(Arrays.asList(EAPIProvider.values()));
    }
}
