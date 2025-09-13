package in.edu.jspmjscoe.admissionportal.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntranceExamDTO {

    private Long entranceExamId;    // id of the exam row

    private Long studentId;         // we only expose studentId, not the whole Student entity

    private String examType;        // JEE / CET

    private String examNo;          // Application/Roll No

    private Double percentile;
}
