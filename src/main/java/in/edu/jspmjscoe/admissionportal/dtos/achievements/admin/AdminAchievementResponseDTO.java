package in.edu.jspmjscoe.admissionportal.dtos.achievements.admin;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAchievementResponseDTO<T> {

    private String achievementType;
    private List<StudentAchievementDTO<T>> students;
}
