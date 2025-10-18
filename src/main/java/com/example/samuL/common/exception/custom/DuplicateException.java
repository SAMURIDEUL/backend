package com.example.samuL.common.exception.custom;

import com.example.samuL.common.exception.customBase.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateException extends CustomException {
    public DuplicateException(String message){
        super(message, HttpStatus.BAD_REQUEST);
    }
}
