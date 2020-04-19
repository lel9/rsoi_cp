package ru.bmstu.cp.rsoi.patient.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.bmstu.cp.rsoi.patient.exception.NoSuchPatientException;
import ru.bmstu.cp.rsoi.patient.exception.NoSuchReceptionException;
import ru.bmstu.cp.rsoi.patient.exception.PatientAlreadyExistsException;
import ru.bmstu.cp.rsoi.patient.model.GenericResponse;

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
    @ExceptionHandler({NoSuchPatientException.class})
    public ResponseEntity<Object> handleNoSuchPatient(final NoSuchPatientException ex, final WebRequest request) {
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "NoSuchPatient");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({NoSuchReceptionException.class})
    public ResponseEntity<Object> handleNoSuchReception(final NoSuchReceptionException ex, final WebRequest request) {
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "NoSuchReception");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 409
    @ExceptionHandler({ PatientAlreadyExistsException.class })
    public ResponseEntity<Object> handleDrugAlreadyExists(final PatientAlreadyExistsException ex, final WebRequest request) {
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "PatientAlreadyExists");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        ex.printStackTrace();
        final GenericResponse bodyOfResponse = new GenericResponse("Внутренняя ошибка сервера", "InternalError");
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

