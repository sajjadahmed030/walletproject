package com.example.mywallet.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;



@Getter
public class Exception extends RuntimeException {
    private  HttpStatus status;
    public Exception(String message, HttpStatus status) {
        super(message);
        this.status=status;

    }

}
