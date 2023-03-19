package com.blog.api.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.model.Blog;
import com.blog.api.model.BlogListResponse;
import com.blog.api.model.EBlogSort;
import com.blog.api.service.BlogService;

@RestController
@RequestMapping("blogs")
public class BlogRestController {

    private final BlogService blogService;

    public BlogRestController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public ResponseEntity<BlogListResponse> retrieveBlogs(
        @RequestParam String keyword,
        @RequestParam(required = false, defaultValue = "ACCURACY") EBlogSort sort,
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "10") int size
    ) {
        List<Blog> blogs = null;
        try {
            blogs = blogService.retrieveBlogs(keyword, sort, page, size);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        
        return ResponseEntity.ok().body(new BlogListResponse(blogs));
    }


}
