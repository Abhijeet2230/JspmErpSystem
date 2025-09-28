package in.edu.jspmjscoe.admissionportal.dtos.student;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.competition.CompetitionDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.fieldproject.FieldProjectDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.internship.InternshipDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.miniproject.MiniProjectDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAcademicYearDTO {

    private Long studentAcademicYearId;
    private Long studentId;         // Reference to Student

    private Integer yearOfStudy;    // 1 = FE, 2 = SE, etc.
    private Integer semester;       // 1..8
    private String batchYear;      // Admission batch or academic year
    private String division;        // A, B, C, etc.
    private Integer rollNo;         // Roll number in division

    private LocalDate semesterStartDate;
    private LocalDate semesterEndDate;

    private Boolean isActive;       // Current semester or not

    private List<CertificateDTO> certificates;
    private List<InternshipDTO> internships;

    private List<MiniProjectDTO> miniProjects;

    private List<FieldProjectDTO> fieldProjects;

    private List<CompetitionDTO> competitions;

}
