package com.flint.sample_be_springboot.dto.exam;

import com.flint.sample_be_springboot.entity.exam.ExamEntity;
import lombok.Data;

@Data
public class ExamNoticeDTO {

    private Long examResultId;
    private ExamEntity examEntity;
    private String noticeName;
    private String noticeData;

}
