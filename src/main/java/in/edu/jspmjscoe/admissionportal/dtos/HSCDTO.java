package in.edu.jspmjscoe.admissionportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HSCDTO {

    private Long hscId;
    private Long studentId;   // only FK reference, not full Student

    private String board;
    private Integer passingYear;
    private String seatNo;

    private Double physicsPercentage;
    private Double chemistryPercentage;
    private Double mathPercentage;

    private String additionalSubjectName;
    private Double additionalSubjectPercentage;

    private Double englishPercentage;
    private Double totalPercentage;
    private Double eligibilityPercentage;
}
