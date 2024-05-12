package com.starlightsymphony.event.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {

    private String status;
    private String code;
    private String description;

    public ErrorResponse() {

    }
    public ErrorResponse(String status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }

}
