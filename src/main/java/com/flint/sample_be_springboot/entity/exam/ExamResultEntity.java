package com.flint.sample_be_springboot.entity.exam;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;

@Table(name = "EXAM_RESULT_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "EXAM_RESULT_ID")
    private Long examResultId;

    @ManyToOne(targetEntity = ExamEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "EXAM_ID")
    private ExamEntity examEntity;

    @Column(name = "RESULT_NAME")
    private String resultName;

    @Lob
    @Column(name = "RESULT_DATA")
    private byte[] resultData;

    @Embedded
    private AuditDetails auditDetails;

    public String getResultData(){
        if(resultData != null){
            return Base64.getEncoder().encodeToString(resultData);
        }
        return null;
    }

}
