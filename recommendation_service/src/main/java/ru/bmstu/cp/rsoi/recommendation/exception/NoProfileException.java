package ru.bmstu.cp.rsoi.recommendation.exception;

public class NoProfileException extends AppException {

    public NoProfileException() { }

    @Override
    public String getMessage() {
        return "Ошибка при добавлении рекомендации: ФИО не заполнено";
    }
}