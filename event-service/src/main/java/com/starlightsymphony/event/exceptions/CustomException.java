package com.starlightsymphony.event.exceptions;

import lombok.Data;

@Data
public class CustomException extends RuntimeException{

    private static final long serialVersionID = 1L;
    private ErrorResponse errorResponse;


    public CustomException(ErrorResponse errorResponse){
        super();
        this.errorResponse = errorResponse;

    }
}
