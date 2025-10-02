package in.edu.jspmjscoe.admissionportal.services.impl.teacher.appraisal;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.appriasal.TeacherAppraisalMarksDTO;
import in.edu.jspmjscoe.admissionportal.exception.ResourceNotFoundException;
import in.edu.jspmjscoe.admissionportal.exception.TeacherNotFoundException;
import in.edu.jspmjscoe.admissionportal.mappers.teacher.appriasal.TeacherAppraisalMapper;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.appriasal.TeacherAppraisal;
import in.edu.jspmjscoe.admissionportal.repositories.security.UserRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.TeacherRepository;
import in.edu.jspmjscoe.admissionportal.repositories.teacher.appriasal.TeacherAppraisalRepository;
import in.edu.jspmjscoe.admissionportal.services.impl.achievements.MinioStorageService;
import in.edu.jspmjscoe.admissionportal.services.teacher.appraisal.TeacherAppraisalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherAppraisalServiceImpl implements TeacherAppraisalService {

    private final TeacherAppraisalRepository teacherAppraisalRepository;
    private final MinioStorageService minioStorageService;
    private final TeacherAppraisalMapper teacherAppraisalMapper;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @Override
    public TeacherAppraisalDTO createAppraisal(TeacherAppraisalDTO dto,
                                               @AuthenticationPrincipal UserDetails userDetails,
                                               MultipartFile document,
                                               MultipartFile photo,
                                               MultipartFile video) {

        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Teacher teacher = teacherRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        // 1️⃣ Fetch the full Teacher entity based on teacherId
        Teacher teacherEntity = teacherRepository.findById(teacher.getTeacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with ID: " + dto.getTeacherId()));

        // Build full name and set in DTO for response
        String fullName = String.join(" ",
                teacherEntity.getFirstName() != null ? teacherEntity.getFirstName() : "",
                teacherEntity.getMiddleName() != null ? teacherEntity.getMiddleName() : "",
                teacherEntity.getLastName() != null ? teacherEntity.getLastName() : ""
        ).trim();

        dto.setTeacherName(fullName);
        dto.setDepartmentName(teacherEntity.getDepartment() != null ? teacherEntity.getDepartment().getName() : null);

        // 2️⃣ Handle file uploads via MinIO
        if (document != null) {
            String docKey = minioStorageService.generateObjectKey("teacher-appraisal-docs",
                    dto.getTeacherId(), document.getOriginalFilename());
            dto.setAppraisalDocumentPath(minioStorageService.uploadTeacherAppraisalFile(document, docKey));
        }

        if (photo != null) {
            String photoKey = minioStorageService.generateObjectKey("teacher-appraisal-photos",
                    dto.getTeacherId(), photo.getOriginalFilename());
            dto.setActivityPhotoPath(minioStorageService.uploadTeacherAppraisalFile(photo, photoKey));
        }

        if (video != null) {
            String videoKey = minioStorageService.generateObjectKey("teacher-appraisal-videos",
                    dto.getTeacherId(), video.getOriginalFilename());
            dto.setActivityVideoPath(minioStorageService.uploadTeacherAppraisalFile(video, videoKey));
        }

        // 3️⃣ Map DTO to entity and set full Teacher entity
        TeacherAppraisal entity = teacherAppraisalMapper.toEntity(dto);
        entity.setTeacher(teacherEntity); // ✅ ensures MapStruct can use firstName/lastName

        // 4️⃣ Save entity
        TeacherAppraisal saved = teacherAppraisalRepository.save(entity);

        // 5️⃣ Convert saved entity to DTO (teacherName and departmentName now resolved)
        return teacherAppraisalMapper.toDTO(saved);
    }

    @Override
    public TeacherAppraisalDTO getAppraisalById(Long appraisalId) {
        TeacherAppraisal appraisal = teacherAppraisalRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException("Appraisal not found with ID: " + appraisalId));

        TeacherAppraisalDTO dto = teacherAppraisalMapper.toDTO(appraisal);

        // Generate presigned URLs
        dto.setAppraisalDocumentPath(
                dto.getAppraisalDocumentPath() != null ?
                        minioStorageService.getPresignedUrlTeacher(dto.getAppraisalDocumentPath()) : null
        );

        dto.setActivityPhotoPath(
                dto.getActivityPhotoPath() != null ?
                        minioStorageService.getPresignedUrlTeacher(dto.getActivityPhotoPath()) : null
        );

        dto.setActivityVideoPath(
                dto.getActivityVideoPath() != null ?
                        minioStorageService.getPresignedUrlTeacher(dto.getActivityVideoPath()) : null
        );

        return dto;
    }

    @Override
    public List<TeacherAppraisalDTO> getAllAppraisals() {
        return teacherAppraisalRepository.findAll()
                .stream()
                .map(appraisal -> {
                    TeacherAppraisalDTO dto = teacherAppraisalMapper.toDTO(appraisal);

                    dto.setAppraisalDocumentPath(
                            dto.getAppraisalDocumentPath() != null ?
                                    minioStorageService.getPresignedUrlTeacher(dto.getAppraisalDocumentPath()) : null
                    );
                    dto.setActivityPhotoPath(
                            dto.getActivityPhotoPath() != null ?
                                    minioStorageService.getPresignedUrlTeacher(dto.getActivityPhotoPath()) : null
                    );
                    dto.setActivityVideoPath(
                            dto.getActivityVideoPath() != null ?
                                    minioStorageService.getPresignedUrlTeacher(dto.getActivityVideoPath()) : null
                    );

                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional
    public List<TeacherAppraisalDTO> bulkUpdateMarks(List<TeacherAppraisalMarksDTO> marksList) {
        List<TeacherAppraisalDTO> updatedAppraisals = marksList.stream().map(m -> {
            TeacherAppraisal appraisal = teacherAppraisalRepository.findById(m.getAppraisalId())
                    .orElseThrow(() -> new RuntimeException("Appraisal not found with ID: " + m.getAppraisalId()));

            appraisal.setMarks(m.getMarks()); // entity is managed, no need for save()
            return teacherAppraisalMapper.toDTO(appraisal);
        }).toList();

        teacherAppraisalRepository.flush(); // 🔥 force changes to DB

        return updatedAppraisals;
    }

    @Override
    public List<TeacherAppraisalDTO> getAppraisalsByTeacher(Long teacherId) {
        return teacherAppraisalRepository.findByTeacher_TeacherId(teacherId)
                .stream().map(teacherAppraisalMapper::toDTO).toList();
    }

}
