package com.flint.sample_be_springboot.entity.websiteModuleEntities.admission;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "ADMISSION_PROCESS_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdmissionProcessEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMISSION_PROCESS_ID")
    private Long admissionProcessId;

    @ManyToOne(targetEntity = AdmissionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMISSION_ID")
    private AdmissionEntity admissionEntity;

    @Column(name = "STEP_NO")
    private String stepNo;

    @Column(name = "HEADING")
    private String heading;

    @Column(name = "DESCRIPTION")
    private String description;

    @Embedded
    private AuditDetails auditDetails;

}
