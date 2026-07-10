package com.flint.sample_be_springboot.entity.exam;

import jakarta.persistence.*;
import lombok.*;

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
    private Long examResultEntity;

    @ManyToOne(targetEntity = ExamEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "EXAM_ID")
    private ExamEntity examEntity;

    @Column(name = "RESULT_NAME")
    private String resultName;

    @Lob
    @Column(name = "RESULT_DATA")
    private byte[] resultData;

}
