package com.starlightsymphony.event.exceptions;

import com.starlightsymphony.event.utils.EventConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionAdvicer {
    public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvicer.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> validationError(CustomException customException) {
        LOGGER.error("VALIDATION ERROR {} {}", customException.getErrorResponse().getCode(),
                customException.getErrorResponse().getDescription());

        Map<String, Object> map = new HashMap<>();
        map.put(EventConstants.PAYLOAD, customException.getErrorResponse());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> generalError(Exception exception) {
        LOGGER.error(EventConstants.FATAL_ERROR + exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                EventConstants.ERROR, "1010", "general error");
        Map<String, Object> map = new HashMap<>();
        map.put(EventConstants.PAYLOAD, errorResponse);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}