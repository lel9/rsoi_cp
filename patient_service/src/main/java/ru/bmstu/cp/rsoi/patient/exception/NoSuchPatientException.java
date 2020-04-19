package ru.bmstu.cp.rsoi.patient.exception;

public final class NoSuchPatientException extends AppException {

    public NoSuchPatientException() { }

    @Override
    public String getMessage() {
        return "Пациент не найден";
    }
}