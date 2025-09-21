package in.edu.jspmjscoe.admissionportal.dtos.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;

// DTO for result
@Data
@AllArgsConstructor
public  class CceInitResult {
        private int unitRowsCreated;
        private int examRowsCreated;
        private int attendanceRowsCreated;
    }