package com.flint.sample_be_springboot.entity;


import com.flint.sample_be_springboot.enums.StudentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Table(name = "ADMISSION_INQUIRY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdmissionInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "ADMISSION_INQUIRY_ID")
    private Long admissionInquiryId;

    @NotNull
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotNull
    @Column(name = "LAST_NAME")
    private String lastName;

    @NotNull
    @Column(name = "CONTACT_NUMBER")
    private String contactNumber;

    @Column(name = "STANDARD")
    private String standard;

    @Column(name = "MEDIUM")
    private String medium;

    @Column(name = "SECTION")
    private String section;

    @Column(name = "INQUIRY_DATE")
    private LocalDate inquiryDate;

    @Column(name = "STREAM")
    private String stream;


    @Column(name = "STATUS")
    private String status;

    @Embedded
    private AuditDetails auditDetails;


}
