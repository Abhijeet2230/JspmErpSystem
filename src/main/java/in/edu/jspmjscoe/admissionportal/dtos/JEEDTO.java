package in.edu.jspmjscoe.admissionportal.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JEEDTO {

    private Long jeeId;
    private Long studentId;      // instead of whole Student object
    private String applicationNo;
    private Double percentile;
}
