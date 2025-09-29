package in.edu.jspmjscoe.admissionportal.dtos.achievements.admin;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAchievementDTO<T>{
    private BaseStudentAchievementDTO studentInfo;
    private List<T> achievements; // can hold any type of achievement DTO
}
