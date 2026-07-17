package com.flint.sample_be_springboot.entity.websiteModuleEntities.admission;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "IMPORTANT_DATE_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImportantDateEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMPORTANT_DATE_ID")
    private Long importantDateId;

    @ManyToOne(targetEntity = AdmissionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMISSION_ID")
    private AdmissionEntity admissionEntity;

    @Column(name = "EVENT_NAME")
    private String eventName;

    @Column(name = "EVENT_DATE")
    private LocalDate eventDate;

    @Embedded
    private AuditDetails auditDetails;

}
