package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "CLASS_SUBJECT_ALLOCATION")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassSubjectAllocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "CLASS_SUBJECT_ALLOCATION_ID")
    private Long classSubjectAllocationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLASS_MASTER_ID")
    private ClassMasterEntity classMasterEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBJECT_MASTER_ID")
    private SubjectMasterEntity subjectMasterEntity;

}
