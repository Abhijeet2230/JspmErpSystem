package in.edu.jspmjscoe.admissionportal.mappers.trainingplacement;

import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.TrainingPlacementRecordDTO;
import in.edu.jspmjscoe.admissionportal.dtos.trainingplacement.TrainingPlacementTestDTO;
import in.edu.jspmjscoe.admissionportal.model.trainingplacement.*;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingPlacementMapper {

    // ---------- Record ----------
    @Mapping(source = "studentAcademicYear.studentAcademicYearId", target = "studentAcademicYearId")
    TrainingPlacementRecordDTO toDTO(TrainingPlacementRecord record);

    @Mapping(source = "studentAcademicYearId", target = "studentAcademicYear.studentAcademicYearId")
    TrainingPlacementRecord toEntity(TrainingPlacementRecordDTO dto);

    List<TrainingPlacementRecordDTO> toDTOList(List<TrainingPlacementRecord> records);

    // ---------- Test ----------
    TrainingPlacementTestDTO toDTO(TrainingPlacementTest test);

    TrainingPlacementTest toEntity(TrainingPlacementTestDTO dto);

    List<TrainingPlacementTestDTO> toDTOTestList(List<TrainingPlacementTest> tests);
}
