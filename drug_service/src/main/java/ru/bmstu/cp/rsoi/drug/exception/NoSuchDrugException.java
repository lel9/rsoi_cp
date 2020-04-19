package ru.bmstu.cp.rsoi.drug.exception;

public final class NoSuchDrugException extends AppException {

    public NoSuchDrugException() { }

    @Override
    public String getMessage() {
        return "Препарат не найден";
    }
}