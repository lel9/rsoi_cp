package ru.bmstu.cp.rsoi.recommendation.exception;

public final class NoSuchRecommendationException extends AppException {

    public NoSuchRecommendationException() { }

    @Override
    public String getMessage() {
        return "Экспертная рекоммендация к приему препарата не найдена";
    }
}