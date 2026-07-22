package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "TEACHER_CLASS_SUBJECT_ALLOCATION")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherClassSubjectAllocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEACHER_CLASS_SUBJECT_ALLOCATION_ID")
    private Long teacherClassSubjectAllocationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_INFORMATION_ID")
    private UserInformationEntity userInformationEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLASS_MASTER_ID")
    private ClassMasterEntity classMasterEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBJECT_MASTER_ID")
    private SubjectMasterEntity subjectMasterEntity;

}
