package ru.bmstu.cp.rsoi.recommendation.exception;

public class FailureWhenRecommendationAddException extends AppException {

    public FailureWhenRecommendationAddException() { }

    @Override
    public String getMessage() {
        return "Ошибка при добавлении рекомендации: не удалось получить ФИО пользователя";
    }
}
