package com.managementProject.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class AppException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;
    private Object data;

    public AppException(String message,HttpStatus httpStatus){
        this(message,httpStatus,null);
    }
    public AppException(String message, HttpStatus httpStatus, Object data){
        this.message=message;
        this.data=data;
        this.httpStatus=httpStatus;
    }
}
