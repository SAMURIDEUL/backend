package com.example.samuL.common.exception.custom;

import com.example.samuL.common.exception.customBase.CustomException;
import org.springframework.http.HttpStatus;

public class HeaderException extends CustomException {
    public HeaderException(String message){
        super(message, HttpStatus.BAD_REQUEST);
    }
}
