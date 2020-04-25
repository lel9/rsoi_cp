package ru.bmstu.cp.rsoi.drug.exception;

public class DrugsNotFoundException extends AppException {

    public DrugsNotFoundException() { }

    @Override
    public String getMessage() {
        return "Препараты не найдены";
    }
}
