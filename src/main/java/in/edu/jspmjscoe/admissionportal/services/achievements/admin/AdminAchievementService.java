package in.edu.jspmjscoe.admissionportal.services.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.admin.AdminAchievementResponseDTO;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;

import java.util.List;

public interface AdminAchievementService {


    <T> AdminAchievementResponseDTO<T> getAchievements( String achievementType,
                                                        Integer yearOfStudy,
                                                        Integer semester,
                                                        String department,
                                                        String course,
                                                        String division,
                                                        Long subjectId
    ) ;
}
