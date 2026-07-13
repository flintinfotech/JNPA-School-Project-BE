package com.flint.sample_be_springboot.entity.admission;

import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.entity.classRoom.ClassRoomEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "REQUIRED_DOCUMENT_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequiredDocumentEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUIRED_DOCUMENT_ID")
    private Long requiredDocumentId;

    @ManyToOne(targetEntity = AdmissionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMISSION_ID")
    private AdmissionEntity admissionEntity;

    @Column(name = "DOCUMENT_NAME")
    private String documentName;

    @Embedded
    private AuditDetails auditDetails;

}
