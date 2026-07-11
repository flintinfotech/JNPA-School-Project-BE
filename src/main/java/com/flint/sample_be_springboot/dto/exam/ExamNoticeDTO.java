package com.flint.sample_be_springboot.dto.exam;

import lombok.Data;

@Data
public class ExamNoticeDTO {

    private Long examNoticeId;
    private Long examId;
    private String noticeName;
    private String noticeData;

}
