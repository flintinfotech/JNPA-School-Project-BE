package com.flint.sample_be_springboot.entity.websiteModuleEntities.exam;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;

@Table(name = "TOPPERS_ENTITY")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToppersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "TOPPER_ID")
    private Long topperId;

    @ManyToOne(targetEntity = ExamEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "EXAM_ID")
    private ExamEntity examEntity;

    @Column(name = "SECTION")
    private String section;

    @Column(name = "MEDIUM")
    private String medium;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "STD")
    private String std;

    @Column(name = "DESCRIPTION")
    private String description;

    @Lob
    @Column(name = "STUDENT_IMAGE")
    private byte[] studentImage;

    @Embedded
    private AuditDetails auditDetails;

    public String getStudentImage(){
        if(studentImage != null){
            return Base64.getEncoder().encodeToString(studentImage);
        }
        return null;
    }

}
