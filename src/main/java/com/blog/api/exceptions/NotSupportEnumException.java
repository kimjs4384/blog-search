package com.blog.api.exceptions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

public class NotSupportEnumException extends RuntimeException {

    @Getter
    String supportEnumValues;
    
    public NotSupportEnumException(String message, Enum<?>[] supportEnum) {
        super(message);

        List<String> enumNames =Arrays.asList(supportEnum).stream().map( e -> {
            return e.name();
        }).collect(Collectors.toList());
        this.supportEnumValues = String.join(",", enumNames);
    }

}
