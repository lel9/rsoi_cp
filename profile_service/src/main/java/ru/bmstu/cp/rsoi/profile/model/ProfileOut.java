package ru.bmstu.cp.rsoi.profile.model;

import lombok.Data;

@Data
public class ProfileOut {

    private String id;

    private String displayName;

    private String organization;

    private String profession;
}
