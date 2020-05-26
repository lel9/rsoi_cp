package ru.bmstu.cp.rsoi.analyzer.model;

import lombok.Data;

@Data
public class Token {

    private String access_token;

    private String token_type;

    private String expires_in;

    private String scope;

}
