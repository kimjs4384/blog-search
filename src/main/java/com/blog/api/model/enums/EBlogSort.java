package com.blog.api.model.enums;

import lombok.Getter;

public enum EBlogSort {
    ACCURACY("accuracy"), RECENCY("recency");

    @Getter
    String value;

    EBlogSort(String value) {
        this.value = value;
    }
}
