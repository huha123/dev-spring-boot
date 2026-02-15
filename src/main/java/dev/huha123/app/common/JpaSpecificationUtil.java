package dev.huha123.app.common;

import java.util.Collection;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Path;

public class JpaSpecificationUtil {

    public static <T> Specification<T> emptySpec() {
        return (root, query, builder) -> builder.conjunction();
    }

    public static <T> Specification<T> equal(String attributeName, Object value) {
        return (root, query, builder) -> {
            if (value == null) {
                return null;
            }
            return builder.equal(getPath(root, attributeName), value);
        };
    }

    public static <T> Specification<T> notEqual(String attributeName, Object value) {
        return (root, query, builder) -> {
            if (value == null) {
                return null;
            }
            return builder.notEqual(getPath(root, attributeName), value);
        };
    }

    public static <T> Specification<T> like(String attributeName, String value) {
        return (root, query, builder) -> {
            if (!StringUtils.hasText(value)) {
                return null;
            }
            return builder.like(getPath(root, attributeName).as(String.class), "%" + value + "%");
        };
    }

    public static <T> Specification<T> likeIgnoreCase(String attributeName, String value) {
        return (root, query, builder) -> {
            if (!StringUtils.hasText(value)) {
                return null;
            }
            return builder.like(builder.lower(getPath(root, attributeName).as(String.class)),
                    "%" + value.toLowerCase() + "%");
        };
    }

    public static <T> Specification<T> in(String attributeName, Collection<?> values) {
        return (root, query, builder) -> {
            if (values == null || values.isEmpty()) {
                return null;
            }
            return getPath(root, attributeName).in(values);
        };
    }

    @SuppressWarnings("unchecked")
    public static <T, Y extends Comparable<? super Y>> Specification<T> between(
            String attributeName, Y min, Y max) {
        return (root, query, builder) -> {
            if (min == null && max == null) {
                return null;
            }
            Path<Y> path = (Path<Y>) getPath(root, attributeName);
            if (min != null && max != null) {
                return builder.between(path, min, max);
            }
            if (min != null) {
                return builder.greaterThanOrEqualTo(path, min);
            }
            return builder.lessThanOrEqualTo(path, max);
        };
    }

    private static Path<?> getPath(Path<?> path, String attributeName) {
        if (attributeName.contains(".")) {
            String[] split = attributeName.split("\\.");
            Path<?> currentPath = path;
            for (String part : split) {
                currentPath = currentPath.get(part);
            }
            return currentPath;
        }
        return path.get(attributeName);
    }
}
