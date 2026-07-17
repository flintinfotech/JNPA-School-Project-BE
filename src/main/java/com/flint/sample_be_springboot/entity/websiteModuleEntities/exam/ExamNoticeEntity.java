package com.flint.sample_be_springboot.entity.websiteModuleEntities.exam;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;

@Table(name = "EXAM_NOTICE_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamNoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "EXAM_RESULT_ID")
    private Long examNoticeId;

    @ManyToOne(targetEntity = ExamEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "EXAM_ID")
    private ExamEntity examEntity;

    @Column(name = "NOTICE_NAME")
    private String noticeName;

    @Lob
    @Column(name = "NOTICE_DATA")
    private byte[] noticeData;

    @Embedded
    private AuditDetails auditDetails;

    public String getNoticeData(){
        if(noticeData != null){
            return Base64.getEncoder().encodeToString(noticeData);
        }
        return null;
    }

}
