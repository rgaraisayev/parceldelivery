package com.guavapay.authservice.model.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericException extends RuntimeException {

    private String message;
    private Integer statusCode;
    private String label;

    public GenericException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
