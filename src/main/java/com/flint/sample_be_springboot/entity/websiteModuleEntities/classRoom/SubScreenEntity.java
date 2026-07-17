package com.flint.sample_be_springboot.entity.websiteModuleEntities.classRoom;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "SUB_SCREEN_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubScreenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "SUB_SCREEN_ID")
    private Long subScreenId;

    @ManyToOne(targetEntity = AcademicYearEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACADEMIC_YEAR_ID")
    private AcademicYearEntity academicYearEntity;

    @Column(name = "SUB_SCREEN_NAME")
    private String subScreenName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "subScreenEntity")
    private List<SubScreenDataEntity> subScreenDataEntities;

}
