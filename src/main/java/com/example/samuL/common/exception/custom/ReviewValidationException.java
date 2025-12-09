package com.example.samuL.common.exception.custom;

import com.example.samuL.common.exception.customBase.CustomException;
import org.springframework.http.HttpStatus;

public class ReviewValidationException extends CustomException {
    public ReviewValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
