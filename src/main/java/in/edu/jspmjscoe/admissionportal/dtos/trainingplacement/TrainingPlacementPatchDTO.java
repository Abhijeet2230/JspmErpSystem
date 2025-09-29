package in.edu.jspmjscoe.admissionportal.dtos.trainingplacement;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingPlacementPatchDTO {

    private Long studentAcademicYearId;   // ✅ instead of studentId

    // core fields
    private Double sgpaScore;
    private Double softskillAttendance;
    private Double certificationCourses;
    private Double allSubjectQuiz;
    private Double internship;
    private Double courseProject;
    private Double nationalEvent;
    private Double totalScore;

    // test scores to update
    private List<TrainingPlacementTestDTO> tests;
}
