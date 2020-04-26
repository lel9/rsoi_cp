package ru.bmstu.cp.rsoi.recommendation.web.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.bmstu.cp.rsoi.recommendation.exception.NoSuchRecommendationException;
import ru.bmstu.cp.rsoi.recommendation.exception.RecommendationsNotFoundException;
import ru.bmstu.cp.rsoi.recommendation.model.GenericResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public RestResponseEntityExceptionHandler() {
        super();
    }

    // 400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final BindingResult result = ex.getBindingResult();
        final GenericResponse bodyOfResponse = new GenericResponse(result.getAllErrors(), "Invalid" + result.getObjectName());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 404
    @ExceptionHandler({NoSuchRecommendationException.class})
    public ResponseEntity<Object> handleNoSuchRecommendation(final NoSuchRecommendationException ex, final WebRequest request) {
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "NoSuchRecommendation");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({RecommendationsNotFoundException.class})
    public ResponseEntity<Object> handleRecommendationsNotFoundException(final RecommendationsNotFoundException ex, final WebRequest request) {
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "RecommendationsNotFound");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 503
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Object> handleResourceAccessException(final ResourceAccessException ex, final WebRequest request) {
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "TimeOut");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        ex.printStackTrace();
        final GenericResponse bodyOfResponse = new GenericResponse("Внутренняя ошибка сервера", "InternalError");
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

