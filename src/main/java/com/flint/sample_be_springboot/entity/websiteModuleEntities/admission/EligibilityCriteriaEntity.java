package com.flint.sample_be_springboot.entity.websiteModuleEntities.admission;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "ELIGIBILITY_CRITERIA_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EligibilityCriteriaEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ELIGIBILITY_CRITERIA_ID")
    private Long eligibilityCriteriaId;

    @ManyToOne(targetEntity = AdmissionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMISSION_ID")
    private AdmissionEntity admissionEntity;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Embedded
    private AuditDetails auditDetails;

}
