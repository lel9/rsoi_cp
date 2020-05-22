package ru.bmstu.cp.rsoi.recommendation.model;

import lombok.Data;

@Data
public class ProfileIn {

    private String id;

    private String displayName;

    @Override
    public String toString() {
        return "ProfileIn{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
