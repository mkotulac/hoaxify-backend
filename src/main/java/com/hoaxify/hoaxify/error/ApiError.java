package com.hoaxify.hoaxify.error;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * Created by Martin Kotulac
 * on 07/12/2020
 */
@Data
@NoArgsConstructor
public class ApiError {

    private final long timestamp = new Date().getTime();
    private int status;
    private String message;
    private String url;
    private Map<String, String> validationErrors;

    public ApiError(int status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }
}
