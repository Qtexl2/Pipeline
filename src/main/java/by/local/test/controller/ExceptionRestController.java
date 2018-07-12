package by.local.test.controller;

import by.local.test.exception.ApiError;
import by.local.test.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "by.local.test.controller")
public class ExceptionRestController {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException e) {
        return new ResponseEntity<>(ApiError.builder().errorCode(e.getHttpStatus().value()).errorStatus(e.getMessage()).build(),e.getHttpStatus());
    }
}
