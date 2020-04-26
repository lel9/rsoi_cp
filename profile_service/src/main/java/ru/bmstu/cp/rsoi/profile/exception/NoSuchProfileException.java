package ru.bmstu.cp.rsoi.profile.exception;

public final class NoSuchProfileException extends AppException {

    public NoSuchProfileException() { }

    @Override
    public String getMessage() {
        return "Профиль пользователя не найден";
    }
}