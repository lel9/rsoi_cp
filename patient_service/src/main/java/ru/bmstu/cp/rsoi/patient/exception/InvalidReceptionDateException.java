package ru.bmstu.cp.rsoi.patient.exception;

public class InvalidReceptionDateException extends AppException {

    public InvalidReceptionDateException() {
    }

    @Override
    public String getMessage() {
        return "Дата осмотра не может быть меньше даты рождения пациента";
    }
}
