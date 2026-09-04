package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Base64;

@Entity
@Table(name = "HOMEWORK_ENTITY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HomeworkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "HOMEWORK_ID")
    private Long homeworkId;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "STANDARD")
    private String standard;

    @Column(name = "DIVISION")
    private String division;

    @Column(name = "MEDIUM")
    private String medium;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @Column(name = "HOMEWORK_DATE")
    private LocalDate homeworkDate;

    @Column(name = "REMARK")
    private String remark;

    @Lob
    @Column(name = "UPLOADED_FILE")
    private byte[] uploadedFile;

    @Embedded
    private AuditDetails auditDetails;

    public String getUploadedFile() {
        if (uploadedFile != null) {
            return Base64.getEncoder().encodeToString(uploadedFile);
        }
        return null;
    }

}
