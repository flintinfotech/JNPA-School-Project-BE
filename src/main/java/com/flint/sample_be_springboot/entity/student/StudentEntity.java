package com.flint.sample_be_springboot.entity.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.enums.StudentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Table(name = "STUDENT_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "STUDENT_ID")
    private Long studentId;

    @NotNull
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotNull
    @Column(name = "LAST_NAME")
    private String lastName;

    @NotNull
    @Column(name = "GENDER")
    private String gender;

    @NotNull
    @Column(name = "DOB")
    private String DOB;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "BLOOD_GROUP")
    private String bloodGroup;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "RELIGION")
    private String religion;

    @Column(name = "CASTE")
    private String caste;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private StudentStatus status;

    @Lob
    @Column(name = "PROFILE_IMG")
    private byte[] profileImg;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<ParentEntity> parentEntities;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<StudentDocumentEntity> studentDocumentEntities;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<AcademicInformationEntity> academicInformationEntity;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studentEntity",cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<StudentFeeEntity> studentFeeEntities;


    @OneToOne(fetch = FetchType.LAZY,mappedBy = "studentEntity",cascade = CascadeType.ALL,orphanRemoval = true)
    private ParentEntity parentEntity;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "studentEntity",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<StudentDocumentEntity> studentDocumentEntities;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "studentEntity",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<AcademicInformationEntity> academicInformationEntity;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "studentEntity",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<StudentFeeEntity> studentFeeEntities;

    @Embedded
    private AuditDetails auditDetails;


}
