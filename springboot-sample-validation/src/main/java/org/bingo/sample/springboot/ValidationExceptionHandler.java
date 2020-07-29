package org.bingo.sample.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @Author: Bin GU
 * @Date: 2019/2/13 17:01
 * @Version Initial Version
 */
@RestControllerAdvice
public class ValidationExceptionHandler {

    /** MethodArgumentNotValidException */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleBindException1(MethodArgumentNotValidException e) throws JsonProcessingException {
        System.out.println("ValidationExceptionHandler: MethodArgumentNotValidException");
        e.getBindingResult().getAllErrors().forEach(System.out::println);
        return "MethodArgumentNotValidException";
    }

    /** ConstraintViolationException */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleBindException2(ConstraintViolationException e) {
        System.out.println("ValidationExceptionHandler: ConstraintViolationException");
        e.getConstraintViolations().forEach(System.out::println);
        return "ConstraintViolationException";
    }
}
