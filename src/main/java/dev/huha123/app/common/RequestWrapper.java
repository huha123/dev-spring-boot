package dev.huha123.app.common;

public record RequestWrapper<T>(T payload, SearchCondition searchCondition) {
}
