package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TIME_TABLE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ACADEMIC_YEAR", "CLASS_MASTER_ID", "DIVISION"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIME_TABLE_ID")
    private Long timeTableId;

    @Column(name = "ACADEMIC_YEAR", nullable = false)
    private String academicYear;

    @ManyToOne
    @JoinColumn(name = "CLASS_MASTER_ID", nullable = false)
    private ClassMasterEntity classMasterEntity;

    @Column(name = "DIVISION", nullable = false)
    private String division;

    @OneToMany(mappedBy = "timeTableEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeTablePeriodEntity> timeTablePeriodEntities;

    @Embedded
    private AuditDetails auditDetails;
}
