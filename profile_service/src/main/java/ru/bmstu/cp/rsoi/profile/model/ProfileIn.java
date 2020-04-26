package ru.bmstu.cp.rsoi.profile.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProfileIn {

    @NotNull(message = "Имя пользователя должно быть задано")
    @NotEmpty(message = "Имя пользователя не должно быть пусто")
    private String displayName;

    private String email;

    @NotNull(message = "Название организации должно быть задано")
    @NotEmpty(message = "Название организации не должно быть пусто")
    private String organization;

    @NotNull(message = "Специальность должна быть задана")
    @NotEmpty(message = "Специальность не должна быть пуста")
    private String profession;

}
