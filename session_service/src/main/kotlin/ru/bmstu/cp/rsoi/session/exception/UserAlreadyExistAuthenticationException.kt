package ru.bmstu.cp.rsoi.session.exception

import org.springframework.security.core.AuthenticationException

class UserAlreadyExistAuthenticationException(msg: String?) : AuthenticationException(msg)