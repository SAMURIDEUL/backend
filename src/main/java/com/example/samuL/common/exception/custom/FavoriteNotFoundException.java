package com.example.samuL.common.exception.custom;

import com.example.samuL.common.exception.customBase.CustomException;
import org.springframework.http.HttpStatus;

public class FavoriteNotFoundException extends CustomException {
    public FavoriteNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
