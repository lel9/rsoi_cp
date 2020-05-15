package ru.bmstu.cp.rsoi.session.web.handler

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.bmstu.cp.rsoi.session.exception.UserAlreadyExistAuthenticationException
import ru.bmstu.cp.rsoi.session.model.GenericResponse

@ControllerAdvice
class RestResponseEntityExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(UserAlreadyExistAuthenticationException::class)])
    fun handlerAnnotationException(ex: UserAlreadyExistAuthenticationException, request: WebRequest) : ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message ?: "", "user_already_exists")
        return handleExceptionInternal(
            ex,
            bodyOfResponse,
            HttpHeaders(),
            HttpStatus.CONFLICT,
            request
        )
    }

}