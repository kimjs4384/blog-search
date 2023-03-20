package com.blog.api.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogListResponse {

    private List<Blog> blogs;
    private boolean hasNext;

    public BlogListResponse() {};

    public BlogListResponse(List<Blog> blogs, boolean hasNext) {
        this.blogs = blogs;
        this.hasNext = hasNext;
    }
}
