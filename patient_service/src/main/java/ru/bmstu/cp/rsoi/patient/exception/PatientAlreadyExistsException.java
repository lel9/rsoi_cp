package ru.bmstu.cp.rsoi.patient.exception;

public final class PatientAlreadyExistsException extends AppException {

    private String cardId;

    public PatientAlreadyExistsException(String cardId) { this.cardId = cardId; }

    @Override
    public String getMessage() {
        return "Пациент с номером карты " + cardId + " уже существует";
    }

}