package com.flint.sample_be_springboot.util;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomQuerySpecification<T> implements Specification<T> {

    private final Map<String, Object> filters;

    private CustomQuerySpecification(Map<String, Object> filters) {
        this.filters = filters;
    }

    public static <T> CustomQuerySpecification<T> getInstance(Map<String, Object> filters) {
        return new CustomQuerySpecification<>(filters);
    }

    private Path<?> resolvePath(Root<?> root, String key) {

        if (key.equals("mobile")) {
            return root.join("userId", JoinType.LEFT).get("mobile");
        }

        if (key.equals("name")) {
            return root.join("userId", JoinType.LEFT).get("name");
        }

        if (key.contains(".")) {        // handle nested fields
            String[] parts = key.split("\\.");
            Path<?> path = root;
            for (String p : parts) {
                path = path.get(p);
            }
            return path;
        }

        // If direct field exists in the entity
        if (root.getModel().getAttributes().stream().anyMatch(a -> a.getName().equals(key))) {
            return root.get(key);
        }

        // If field exists in BaseEntity (superclass)
        try {
            return root.getModel().getJavaType()
                    .getSuperclass()
                    .getDeclaredField(key) != null
                    ? root.get(key)
                    : null;

        } catch (NoSuchFieldException e) {
            return null;
        }
    }


    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (filters == null || filters.isEmpty()) {
            return cb.conjunction();
        }

        for (Map.Entry<String, Object> entry : filters.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            Path<?> path = resolvePath(root, key);

            if (path == null) {
                continue;   // skip unknown fields
            }
            if (filters.containsKey("date") && value instanceof LocalDate ) {
                predicates.add(cb.equal(path.as(LocalDate.class), value));
                continue;
            }


            // ----- range:fieldName -----
            if (key.startsWith("range:")) {
                String field = key.substring(6);

                Map<String, String> dateMap = (Map<String, String>) value;

                try {
                    LocalDate start = LocalDate.parse(dateMap.get("start"));
                    LocalDate end = LocalDate.parse(dateMap.get("end"));
                    predicates.add(cb.between(root.get(field), start, end));

                } catch (Exception e) {
                    throw new RuntimeException("Invalid range format: " + key);
                }

                continue;
            }

            // ----- IN CLAUSE -----
            if (value instanceof List<?>) {
                CriteriaBuilder.In<Object> inList = cb.in(path);
                ((List<?>) value).forEach(inList::value);
                predicates.add(inList);
                continue;
            }

            // ----- LIKE -----
            if (value instanceof String) {
                predicates.add(cb.like(cb.lower(path.as(String.class)), "%" + value.toString().toLowerCase() + "%"));
                continue;
            }

            // ----- Equal -----
            predicates.add(cb.equal(path, value));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }


}

