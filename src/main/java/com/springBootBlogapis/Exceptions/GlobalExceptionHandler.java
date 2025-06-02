package com.springBootBlogapis.Exceptions;

import com.springBootBlogapis.Payloads.ErrorHandlingInstances;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;




@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResouceNotFoundException.class)
    public ResponseEntity<ErrorHandlingInstances> handleResourceNotFound(ResouceNotFoundException exception, WebRequest webrequest){
        ErrorHandlingInstances errordetails = new ErrorHandlingInstances(new Date(), exception.getMessage(),webrequest.getDescription(false));

        return new ResponseEntity<>(errordetails, HttpStatus.NOT_FOUND);


    }


    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorHandlingInstances> handleBlogApiException(BlogAPIException exception, WebRequest webrequest){
        ErrorHandlingInstances errordetails = new ErrorHandlingInstances(new Date(), exception.getMessage(),webrequest.getDescription(false));

        return new ResponseEntity<>(errordetails, HttpStatus.BAD_REQUEST);


    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorHandlingInstances> handleGlobalException(Exception exception, WebRequest webrequest){
        ErrorHandlingInstances errordetails = new ErrorHandlingInstances(new Date(), exception.getMessage() ,webrequest.getDescription(false));

        return new ResponseEntity<>(errordetails, HttpStatus.INTERNAL_SERVER_ERROR);


    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                   WebRequest request) {

        Map<String , String > errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
           String fieldname = ((FieldError) error).getField();
           String message = error.getDefaultMessage();
           errors.put(fieldname,message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);



    }

//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValidException (MethodArgumentNotValidException  exception, WebRequest webrequest){
//
//        Map<String , String > errors = new HashMap<>();
//        exception.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldname = ((FieldError) error).getField();
//            String message = error.getDefaultMessage();
//            errors.put(fieldname,message);
//        });
//
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//
//
//    }

}
