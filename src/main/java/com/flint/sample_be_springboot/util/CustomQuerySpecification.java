package com.flint.sample_be_springboot.util;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;

public class CustomQuerySpecification<T> implements Specification<T> {

    private final Map<String, Object> filters;

    private CustomQuerySpecification(Map<String, Object> filters) {
        this.filters = filters;
    }

    public static <T> CustomQuerySpecification<T> getInstance(Map<String, Object> filters) {
        return new CustomQuerySpecification<>(filters);
    }

    /**
     * Child entity field mapping
     */
    private static final Map<String, String> JOIN_FIELDS = new HashMap<>();

    static {

        // Academic Information
        JOIN_FIELDS.put("admissionNo", "academicInformationEntity");
        JOIN_FIELDS.put("admissionDate", "academicInformationEntity");
        JOIN_FIELDS.put("standard", "academicInformationEntity");
        JOIN_FIELDS.put("section", "academicInformationEntity");
        JOIN_FIELDS.put("rollNo", "academicInformationEntity");
        JOIN_FIELDS.put("academicYear", "academicInformationEntity");

        // Parent
        JOIN_FIELDS.put("relation", "parentEntities");
        JOIN_FIELDS.put("occupation", "parentEntities");
        JOIN_FIELDS.put("annualIncome", "parentEntities");

        // Documents
        JOIN_FIELDS.put("documentName", "studentDocumentEntities");

        // User module example
        JOIN_FIELDS.put("mobile", "userId");
        JOIN_FIELDS.put("name", "userId");

        // UserInformation -> UserEntity
        JOIN_FIELDS.put("role", "userEntity");
        JOIN_FIELDS.put("userName", "userEntity");
        JOIN_FIELDS.put("mobileNo", "userEntity");
        JOIN_FIELDS.put("email", "userEntity");
        JOIN_FIELDS.put("medium", "userEntity");
    }

    /**
     * Resolve entity path automatically.
     */
    private Path<?> resolvePath(Root<?> root, String key) {

        // Child entity field
        if (JOIN_FIELDS.containsKey(key)) {

            String joinName = JOIN_FIELDS.get(key);

            // Check whether current entity actually contains this relationship
            boolean joinExists = root.getModel().getAttributes()
                    .stream()
                    .anyMatch(a -> a.getName().equals(joinName));

            if (joinExists) {
                return root.join(joinName, JoinType.LEFT).get(key);
            }
        }

        // Nested field support (optional)
        if (key.contains(".")) {

            String[] parts = key.split("\\.");

            From<?, ?> from = root;

            for (int i = 0; i < parts.length - 1; i++) {
                from = from.join(parts[i], JoinType.LEFT);
            }

            return from.get(parts[parts.length - 1]);
        }

        // Direct entity field
        if (root.getModel().getAttributes()
                .stream()
                .anyMatch(a -> a.getName().equals(key))) {

            return root.get(key);
        }

        // BaseEntity field
        try {

            root.getModel()
                    .getJavaType()
                    .getSuperclass()
                    .getDeclaredField(key);

            return root.get(key);

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Predicate toPredicate(Root<T> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {

        query.distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (filters == null || filters.isEmpty()) {
            return cb.conjunction();
        }

        for (Map.Entry<String, Object> entry : filters.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            // ---------------- RANGE ----------------

            if (key.startsWith("range:")) {

                String field = key.substring(6);

                Path<?> path = resolvePath(root, field);

                if (path == null) {
                    continue;
                }

                Map<String, String> dateMap = (Map<String, String>) value;

                LocalDate start = LocalDate.parse(dateMap.get("start"));
                LocalDate end = LocalDate.parse(dateMap.get("end"));

                predicates.add(
                        cb.between(
                                path.as(LocalDate.class),
                                start,
                                end
                        )
                );

                continue;
            }

            Path<?> path = resolvePath(root, key);

            if (path == null) {
                continue;
            }

            // ---------------- DATE ----------------

            if (value instanceof LocalDate) {

                predicates.add(
                        cb.equal(
                                path.as(LocalDate.class),
                                value
                        )
                );

                continue;
            }

            // ---------------- IN ----------------

            if (value instanceof List<?>) {

                CriteriaBuilder.In<Object> inClause = cb.in(path);

                ((List<?>) value).forEach(inClause::value);

                predicates.add(inClause);

                continue;
            }

            // ---------------- LIKE ----------------

            if (value instanceof String) {

                predicates.add(

                        cb.equal(
                                cb.lower(path.as(String.class)),
                                value.toString().trim().toLowerCase()
                        )
                );

                continue;
            }

            // ---------------- EQUAL ----------------

            predicates.add(

                    cb.equal(path, value)

            );
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}