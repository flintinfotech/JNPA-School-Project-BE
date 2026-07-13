package com.flint.sample_be_springboot.entity.admission;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;
import java.util.List;

@Table(name = "ADMISSION_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdmissionEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMISSION_ID")
    private Long admissionId;

    @Column(name = "CLASS_ROOM_NAME")
    private String classRoomName;

    @Column(name = "ACADEMIC_YEAR_NAME")
    private String academicYearName;

    @Column(name = "MEDIUM")
    private String medium;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "admissionEntity", fetch = FetchType.LAZY, orphanRemoval = true)
    List<EligibilityCriteriaEntity> eligibilityCriteriaEntities;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "admissionEntity", fetch = FetchType.LAZY, orphanRemoval = true)
    List<ImportantDateEntity> importantDatsEntities;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "admissionEntity", fetch = FetchType.LAZY, orphanRemoval = true)
    List<RequiredDocumentEntity> requireDocumentsEntities;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "admissionEntity", fetch = FetchType.LAZY, orphanRemoval = true)
    List<AdmissionProcessEntity> admissionProcessEntities;

    @Embedded
    private AuditDetails auditDetails;

    @Lob
    @Column(name = "BROCHURE")
    private byte[] brochure;

    public String getBrochure(){
        if(brochure != null){
            return Base64.getEncoder().encodeToString(brochure);
        }
        return null;
    }

}
