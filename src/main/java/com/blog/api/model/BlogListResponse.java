package com.blog.api.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogListResponse {

    private List<Blog> blogs;

    public BlogListResponse(List<Blog> blogs) {
        this.blogs = blogs;
    }

}
