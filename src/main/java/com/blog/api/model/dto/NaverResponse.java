package com.blog.api.model.dto;

import java.util.List;

import com.blog.api.model.Blog;
import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverResponse {

    @JsonAlias(value = "lastBuildDate")
    private String lastBuildDate;
    private float total;
    private int start;
    private int display;
    private List<Item> items;
    
    @Getter
    @Setter
    public static class Item {
        private String title;
        private String link;
        private String description;
        private String bloggername;
        private String bloggerlink;
        private String postdate;

        public Blog toBlog() {
            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setContents(description);
            blog.setUrl(link);
            blog.setBlogname(bloggername);
            blog.setDatetime(postdate);
            return blog;
        }
    }

}
