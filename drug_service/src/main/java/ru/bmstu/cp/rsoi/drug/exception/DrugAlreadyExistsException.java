package ru.bmstu.cp.rsoi.drug.exception;

public final class DrugAlreadyExistsException extends AppException {

    private String tradeName;

    public DrugAlreadyExistsException(String tradeName) { this.tradeName = tradeName; }

    @Override
    public String getMessage() {
        return "Препарат с торговым наименованием " + tradeName + " уже существует";
    }

}