package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "SUBJECT_MASTER_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubjectMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "SUBJECT_MASTER_ID")
    private Long subjectMasterId;

    @Column(name = "SUBJECT_CODE")
    private String subjectCode;

    @Column(name = "SUBJECT_NAME")
    private String subjectName;

    @OneToMany(mappedBy = "subjectMasterEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClassSubjectAllocationEntity> classSubjectAllocations = new ArrayList<>();

    @OneToMany(mappedBy = "subjectMasterEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeacherClassSubjectAllocationEntity> teacherClassSubjectAllocationEntities = new ArrayList<>();

}
