package ru.bmstu.cp.rsoi.patient.exception;

public class NoSuchReceptionException extends AppException {

    public NoSuchReceptionException() { }

    @Override
    public String getMessage() {
        return "Осмотр не найден";
    }

}
