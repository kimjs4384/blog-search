package com.blog.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.model.dto.KeywordDTO;
import com.blog.api.model.dto.KeywordResponse;
import com.blog.api.service.KeywordService;

@RestController
@RequestMapping("/keywords")
public class KeywordRestController {

    private final KeywordService keywordService;

    public KeywordRestController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }
    
    @GetMapping
    public ResponseEntity<KeywordResponse> inquireKeyword() {
        List<KeywordDTO> keywords = keywordService.inquireKeyword();
        return ResponseEntity.ok().body(new KeywordResponse(keywords));
    }

}
