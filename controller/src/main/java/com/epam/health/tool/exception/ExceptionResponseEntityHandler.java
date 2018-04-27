package com.epam.health.tool.exception;

import com.epam.health.tool.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ExceptionResponseEntityHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RetrievingObjectException.class)
    public final ResponseEntity<ErrorDetails> handleRetrievingObjectException(RetrievingObjectException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails( ex.getMessage() );
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( errorDetails );
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<ErrorDetails> handleNullPointerException(RetrievingObjectException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails( ex.getMessage() );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

}
