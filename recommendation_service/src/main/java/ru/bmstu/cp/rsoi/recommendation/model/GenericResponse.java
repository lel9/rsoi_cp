package ru.bmstu.cp.rsoi.recommendation.model;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class GenericResponse {

    private String error_description;
    private String error;

    public GenericResponse(final String message) {
        super();
        this.error_description = message;
    }

    public GenericResponse(String message, String type) {
        this.error_description = message;
        this.error = type;
    }

    public GenericResponse(List<ObjectError> allErrors, String error) {
        this.error = error;
        this.error_description = allErrors
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
