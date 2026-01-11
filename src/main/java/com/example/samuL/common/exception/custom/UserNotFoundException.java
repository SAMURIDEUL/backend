package com.example.samuL.common.exception.custom;

import com.example.samuL.common.exception.customBase.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
