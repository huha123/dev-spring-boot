package dev.huha123.app.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Constant {
    HEADER_NAME("Authorization"),
    HEADER_VALUE("Bearer ");

    private String value;
}
