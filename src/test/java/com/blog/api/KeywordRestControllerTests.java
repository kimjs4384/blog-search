package com.blog.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.blog.api.model.KeywordDTO;
import com.blog.api.model.KeywordResponse;
import com.blog.api.repository.KeywordJPARepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-keyword")
public class KeywordRestControllerTests {

    @LocalServerPort
    int port;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    KeywordJPARepository keywordRepository;
    
    List<String> keywords = Arrays.asList(
        "Spring", "SpringBoot", "카카오", "네이버", "타입스크립트", "SpringCloud", "개발도구", 
        "VSCode", "JPA", "VueJS", "React", "SpringConfig", "MSA", "클라우드서비스"
    );

    @Test
    void testInquireKeyword() throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        for (int i = 0; i < keywords.size(); i++) {
            String keyword = keywords.get(i);
            for (int j = 0; j < i + 1; j++) {
                String url = String.format("http://localhost:%d/blogs?keyword=%s", this.port, keyword);

                HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder(new URI(url)).build(),
                    HttpResponse.BodyHandlers.ofString()
                );
    
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            }
        }

        assertThat(keywordRepository.count()).isEqualTo(keywords.size());

        String url = String.format("http://localhost:%d/keywords", this.port);
        HttpResponse<String> response = client.send(
            HttpRequest.newBuilder(new URI(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        KeywordResponse keywordResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        List<KeywordDTO> resultList = keywordResponse.getKeywords();
        assertThat(resultList.size()).isEqualTo(10);
        assertThat(resultList.get(0).getKeyword()).isEqualTo(keywords.get(keywords.size() - 1));
        assertThat(resultList.get(0).getSearchCount()).isEqualTo(keywords.size());
    }
}
