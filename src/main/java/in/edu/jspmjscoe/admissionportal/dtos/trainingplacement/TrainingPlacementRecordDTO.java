package in.edu.jspmjscoe.admissionportal.dtos.trainingplacement;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingPlacementRecordDTO {

    private Long id;
    private Long studentId; // only ID to avoid loading Student entity

    // Merit
    private Double sgpaScore;

    // Employability
    private Double softskillAttendance;
    private Double certificationCourses;

    // Technical Competency
    private Double allSubjectQuiz;
    private Double internship;
    private Double courseProject;
    private Double nationalEvent;

    // Final score
    private Double totalScore;

    // Tests
    private List<TrainingPlacementTestDTO> tests;
}
