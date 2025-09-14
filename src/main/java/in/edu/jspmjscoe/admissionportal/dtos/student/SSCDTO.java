package in.edu.jspmjscoe.admissionportal.dtos.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSCDTO {

    private Long sscId;
    private Long studentId;   // only FK, not full Student entity

    private String board;
    private String passingYear;
    private String seatNo;
    private Double mathPercentage;
    private Double totalPercentage;
}
