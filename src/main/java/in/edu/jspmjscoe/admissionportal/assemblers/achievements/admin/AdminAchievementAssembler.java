package in.edu.jspmjscoe.admissionportal.assemblers.achievements.admin;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.admin.AdminAchievementResponseDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.admin.BaseStudentAchievementDTO;
import in.edu.jspmjscoe.admissionportal.dtos.achievements.admin.StudentAchievementDTO;
import in.edu.jspmjscoe.admissionportal.mappers.achievements.admin.StudentInfoMapper;
import in.edu.jspmjscoe.admissionportal.model.student.StudentAcademicYear;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AdminAchievementAssembler {

    private final StudentInfoMapper studentInfoMapper;

    public AdminAchievementAssembler(StudentInfoMapper studentInfoMapper) {
        this.studentInfoMapper = studentInfoMapper;
    }

    /**
     * Generic method to assemble AdminAchievementResponseDTO
     *
     * @param achievementType descriptive string like "Certificate", "Internship" etc.
     * @param studentAcademicYears list of StudentAcademicYear entities
     * @param achievementFetcher function to get achievement DTOs for a given StudentAcademicYear
     * @return AdminAchievementResponseDTO<T>
     */

    public <T> AdminAchievementResponseDTO<T> assemble(
            String achievementType,
            List<StudentAcademicYear> studentAcademicYears,
            Function<StudentAcademicYear, List<T>> achievementFetcher
    ) {
        List<StudentAchievementDTO<T>> studentAchievements = studentAcademicYears.stream()
                .map(studentYear -> {
                    BaseStudentAchievementDTO baseInfo = studentInfoMapper.toBaseDTO(studentYear);
                    List<T> achievements = achievementFetcher.apply(studentYear);
                    return new StudentAchievementDTO<>(baseInfo, achievements);
                })
                .collect(Collectors.toList());

        return new AdminAchievementResponseDTO<>(achievementType, studentAchievements);
    }
}
