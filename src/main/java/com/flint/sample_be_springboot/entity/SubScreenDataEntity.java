package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;

@Table(name = "SUB_SCREEN_DATA_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubScreenDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "SUB_SCREEN_DATA_ID")
    private Long subScreenDataId;

    @ManyToOne(targetEntity = SubScreenEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "SUB_SCREEN_ID")
    private SubScreenEntity subScreenEntity;

    @Column(name = "SUBJECT_NAME")
    private String subjectName;

    @Lob
    @Column(name = "SUBJECT_DATA")
    private byte[] subjectData;

    public String getSubjectData() {
        if (subjectData != null) {
            return Base64.getEncoder().encodeToString(subjectData);
        }
        return null;
    }

}
