package com.example.samuL.common.exception.custom;

import com.example.samuL.common.exception.customBase.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateFavoriteException extends CustomException {
    public DuplicateFavoriteException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
