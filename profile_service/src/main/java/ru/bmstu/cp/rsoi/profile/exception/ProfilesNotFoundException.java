package ru.bmstu.cp.rsoi.profile.exception;

public class ProfilesNotFoundException extends AppException {

    public ProfilesNotFoundException() { }

    @Override
    public String getMessage() {
        return "Профили не найдены";
    }
}
