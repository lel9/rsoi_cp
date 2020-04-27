package ru.bmstu.cp.rsoi.profile.model;

import lombok.Data;

@Data
public class ProfileOut {

    private String name;

    private String displayName;

    private String email;

    private String organization;

    private String profession;
}
