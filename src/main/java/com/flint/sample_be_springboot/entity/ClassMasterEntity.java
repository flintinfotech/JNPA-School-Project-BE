package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Table(name = "CLASS_MASTER_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "CLASS_MASTER_ID")
    private Long classMasterId;

    @Column(name = "STANDARD")
    private String standard;

    @Column(name = "DIVISION")
    private String division;

    @Column(name = "MEDIUM")
    private String medium;

    @OneToMany(mappedBy = "classMasterEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClassSubjectAllocationEntity> classSubjectAllocations = new ArrayList<>();

    @OneToMany(mappedBy = "classMasterEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeacherClassSubjectAllocationEntity> teacherClassSubjectAllocationEntities = new ArrayList<>();

}
