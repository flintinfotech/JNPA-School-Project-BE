package com.flint.sample_be_springboot.entity.exam;

import jakarta.persistence.*;
import lombok.*;

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
    private Long examResultId;

    @ManyToOne(targetEntity = ExamEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "EXAM_ID")
    private ExamEntity examEntity;

    @Column(name = "NOTICE_NAME")
    private String noticeName;

    @Lob
    @Column(name = "NOTICE_DATA")
    private byte[] noticeData;

}
