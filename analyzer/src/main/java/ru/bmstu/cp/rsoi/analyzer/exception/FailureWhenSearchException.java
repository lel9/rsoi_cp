package ru.bmstu.cp.rsoi.analyzer.exception;

public class FailureWhenSearchException extends AppException {
    public FailureWhenSearchException() { }

    @Override
    public String getMessage() {
        return "Ошибка при поиске препаратов";
    }
}
