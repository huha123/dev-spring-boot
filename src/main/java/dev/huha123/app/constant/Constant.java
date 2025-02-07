package dev.huha123.app.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Constant {
    HEADER_NAME("Authorization"),
    HEADER_VALUE("Bearer "),

    SECURITY_CLAIMS_ID("id"),
    SECURITY_CLAIMS_NAME("name"),
    SECURITY_CLAIMS_EMAIL("email"),
    SECURITY_CLAIMS_AUTH("auth"),
    SECURITY_CLAIMS_ROLE("role")
    ;

    private String value;
}
