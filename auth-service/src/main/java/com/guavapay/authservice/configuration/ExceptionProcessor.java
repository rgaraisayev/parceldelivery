package com.guavapay.authservice.configuration;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.guavapay.authservice.model.exception.GenericException;
import com.guavapay.authservice.model.response.BaseError;
import com.guavapay.authservice.model.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
@Slf4j
public class ExceptionProcessor {


    @ExceptionHandler(GenericException.class)
    public BaseResponse<Void> genericException(HttpServletRequest request, GenericException ex) throws IOException {
        log.info("genericException message={}, code={}", ex.getMessage(), ex.getStatusCode(), ex);
        return new BaseResponse<>(null, new BaseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }


    @ExceptionHandler(value = Exception.class)
    public BaseResponse<Void> exceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("exceptionHandler error occured ", ex);
        if (ex.getCause() != null && ex.getCause() instanceof ConstraintViolationException) {
            handleError((ConstraintViolationException) ex.getCause());
        } else if (ex instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException exception = (HttpMessageNotReadableException) ex;
            InvalidFormatException cause = (InvalidFormatException) exception.getCause();
            return new BaseResponse<>(null, new BaseError(500, "Value " + cause.getValue() + " is not correct"));
        }
        return new BaseResponse<>(null, new BaseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage() == null ? "Error" : ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleError(final TransactionSystemException tse) {
        if (tse.getCause() != null && tse.getCause() instanceof RollbackException) {
            final RollbackException re = (RollbackException) tse.getCause();

            if (re.getCause() != null && re.getCause() instanceof ConstraintViolationException) {
                return handleError((ConstraintViolationException) re.getCause());
            }
        }
        throw tse;
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @SuppressWarnings("unused")
    public ResponseEntity<Object> handleError(final ConstraintViolationException cve) {
        return new ResponseEntity<>(new Object() {
            public String getErrorCode() {
                return "VALIDATION_ERROR";
            }

            public String getMessage() {
                System.out.println(cve.getConstraintName());
                return cve.getConstraintName();
            }
        }, HttpStatus.BAD_REQUEST);
    }

}
