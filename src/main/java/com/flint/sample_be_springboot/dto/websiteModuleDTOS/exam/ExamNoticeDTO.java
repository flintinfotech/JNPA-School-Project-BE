package com.flint.sample_be_springboot.dto.websiteModuleDTOS.exam;

import lombok.Data;

@Data
public class ExamNoticeDTO {

    private Long examNoticeId;
    private Long examId;
    private String noticeName;
    private String noticeData;

}
