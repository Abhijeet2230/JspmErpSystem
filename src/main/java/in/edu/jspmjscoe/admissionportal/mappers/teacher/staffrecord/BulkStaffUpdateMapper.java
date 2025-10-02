package in.edu.jspmjscoe.admissionportal.mappers.teacher.staffrecord;


import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.BulkStaffUpdateDTO;
import in.edu.jspmjscoe.admissionportal.model.teacher.staffrecord.StaffMonthlyReport;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BulkStaffUpdateMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BulkStaffUpdateDTO dto, @MappingTarget StaffMonthlyReport entity);
}
