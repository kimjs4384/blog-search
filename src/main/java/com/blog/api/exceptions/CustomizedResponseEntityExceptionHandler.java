package com.blog.api.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.blog.api.model.dto.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler({
        ConstraintViolationException.class, NotSupportEnumException.class
    })
    public ResponseEntity<ExceptionResponse> handleRequestParamException(Exception exception, WebRequest request) {
        log.debug("handleRequestParamException : {}", request.getDescription(false));

        String message = "Requested param is not valid.";
        List<String> details = new ArrayList<>();
        
        if (exception instanceof ConstraintViolationException) {
            ((ConstraintViolationException) exception).getConstraintViolations().stream().forEach(e -> {
                details.add(e.getMessage());
            });            
        } else if (exception instanceof NotSupportEnumException) {
            details.add(String.format("Suport sort options : %s", ((NotSupportEnumException) exception).getSupportEnumValues()));
        }
        return ResponseEntity.badRequest().body(new ExceptionResponse(message, details));
    }

    @ExceptionHandler({
        APIServerException.class
    })
    public ResponseEntity<ExceptionResponse>  handleAPIServerException(Exception exception, WebRequest request) {
        log.debug("handleAPIServerException : {}", request.getDescription(false));

        return ResponseEntity.internalServerError().body(new ExceptionResponse(exception.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        log.debug("handleMissingServletRequestParameter : {}", request.getDescription(false));

        String message = "Requested param is not valid.";
        String parameterName = ex.getParameterName();
        List<String> details = Arrays.asList(String.format("%s is required option.", parameterName));

        return ResponseEntity.badRequest().body(new ExceptionResponse(message, details));
    }
}
