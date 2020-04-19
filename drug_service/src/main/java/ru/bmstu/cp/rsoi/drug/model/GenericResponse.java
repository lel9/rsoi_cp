package ru.bmstu.cp.rsoi.drug.model;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class GenericResponse {

    private String message;
    private String type;

    public GenericResponse(final String message) {
        super();
        this.message = message;
    }

    public GenericResponse(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public GenericResponse(List<ObjectError> allErrors, String error) {
        this.type = error;
        this.message = allErrors
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
