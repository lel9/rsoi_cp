package ru.bmstu.cp.rsoi.profile.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProfileIn {

    @NotBlank(message = "ФИО должно быть указано")
    private String displayName;

    @NotBlank(message = "Название организации должно быть указано")
    private String organization;

    @NotBlank(message = "Должность должна быть указана")
    private String profession;

}
