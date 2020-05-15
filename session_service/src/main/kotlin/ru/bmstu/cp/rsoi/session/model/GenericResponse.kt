package ru.bmstu.cp.rsoi.session.model

import org.springframework.validation.ObjectError
import java.util.stream.Collectors

class GenericResponse {
    var error_description: String
    var error: String? = null

    constructor(message: String) : super() {
        error_description = message
    }

    constructor(message: String, type: String?) {
        error_description = message
        error = type
    }

    constructor(allErrors: List<ObjectError>, error: String?) {
        this.error = error
        error_description = allErrors
            .stream()
            .map { obj: ObjectError -> obj.defaultMessage }
            .collect(Collectors.joining(System.lineSeparator()))
    }

}