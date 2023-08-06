package com.ann.prophiuslimitedtask.response;

import com.ann.prophiuslimitedtask.error.ValidationError;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private Integer statusCode;
    private HttpStatus status;
    private String reason;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    private Date timestamp;

    private Map<String, List<ValidationError>> validationErrors = new HashMap<>();

}
