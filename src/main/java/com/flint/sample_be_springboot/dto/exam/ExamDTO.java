package com.flint.sample_be_springboot.dto.exam;

import lombok.Data;

import java.util.List;

@Data
public class ExamDTO {

    private Long examId;
    private String result10th;
    private String result12th;
    private String studentScoring90;
    private String universityRank;
    private List<ExamResultDTO> examResultDTOS;
    private List<ExamNoticeDTO> examNoticeDTOS;

}
