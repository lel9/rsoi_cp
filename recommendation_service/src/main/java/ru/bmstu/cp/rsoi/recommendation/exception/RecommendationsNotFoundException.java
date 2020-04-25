package ru.bmstu.cp.rsoi.recommendation.exception;

public class RecommendationsNotFoundException extends AppException {

    public RecommendationsNotFoundException() { }

    @Override
    public String getMessage() {
        return "Экспертные рекомендации к приему препарата не найдены";
    }

}
