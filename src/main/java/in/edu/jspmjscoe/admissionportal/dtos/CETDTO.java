package in.edu.jspmjscoe.admissionportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CETDTO {

    private Long cetId;
    private Long studentId;   // FK reference only
    private String rollNo;
    private Double percentile;
}
