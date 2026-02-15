package dev.huha123.app.common;

public record SearchCondition(
        String title,
        String content,
        String keyword,
        String category,
        String writer,
        String startDate,
        String endDate) {

}
