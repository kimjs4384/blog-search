package com.blog.api.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.exceptions.NotSupportEnumException;
import com.blog.api.model.dto.BlogListResponse;
import com.blog.api.model.enums.EBlogSort;
import com.blog.api.service.BlogService;

@RestController
@RequestMapping("blogs")
@Validated
public class BlogRestController {

    private final BlogService blogService;

    public BlogRestController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public ResponseEntity<BlogListResponse> retrieveBlogs(
        @RequestParam @NotBlank(message = "keyword is required option.") String keyword,
        @RequestParam(required = false, defaultValue = "ACCURACY") String sort,
        @RequestParam(required = false, defaultValue = "1") 
            @Min(value = 1, message = "Minimum page is 1.") 
            @Max(value = 50, message = "Maximum page is 50.") int page,
        @RequestParam(required = false, defaultValue = "10") 
            @Min(value = 1, message = "Minimum size is 1.") 
            @Max(value = 50, message = "Maximum size is 50") int size
    ) {
        BlogListResponse response = null;
        try {
            EBlogSort blogSort = EBlogSort.valueOf(sort);
            response = blogService.retrieveBlogs(keyword, blogSort, page, size);
        } catch (IllegalArgumentException e) {
            throw new NotSupportEnumException(String.format("%s is not supported option", sort), EBlogSort.values());
        }
        
        return ResponseEntity.ok().body(response);
    }


}
