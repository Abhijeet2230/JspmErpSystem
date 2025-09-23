package in.edu.jspmjscoe.admissionportal.mappers.teacher.staffrecord;

import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.StaffMonthlyReportDTO;
import in.edu.jspmjscoe.admissionportal.dtos.teacher.staffrecord.TeacherBasicDTO;

import in.edu.jspmjscoe.admissionportal.model.subject.Department;
import in.edu.jspmjscoe.admissionportal.model.teacher.Teacher;
import in.edu.jspmjscoe.admissionportal.model.teacher.staffrecord.StaffMonthlyReport;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StaffMonthlyReportMapper {

    @Mapping(target = "teacher", expression = "java(mapTeacherToBasicDTO(entity.getTeacher()))")
    StaffMonthlyReportDTO toDto(StaffMonthlyReport entity);

    default StaffMonthlyReport toEntity(StaffMonthlyReportDTO dto) {
        if (dto == null) return null;

        StaffMonthlyReport entity = new StaffMonthlyReport();
        entity.setReportId(dto.getReportId());
        entity.setYear(dto.getYear());
        entity.setMonth(dto.getMonth());
        entity.setDaysInMonth(dto.getDaysInMonth());
        entity.setFirstAndThirdSaturday(dto.getFirstAndThirdSaturday());
        entity.setSundays(dto.getSundays());
        entity.setOtherHolidays(dto.getOtherHolidays());
        entity.setActualWorkingDays(dto.getActualWorkingDays());
        entity.setWorkingOnHoliday(dto.getWorkingOnHoliday());
        entity.setCl(dto.getCl());
        entity.setCompOff(dto.getCompOff());
        entity.setMl(dto.getMl());
        entity.setEl(dto.getEl());
        entity.setOdOrDl(dto.getOdOrDl());
        entity.setLwp(dto.getLwp());
        entity.setSpecialLeave(dto.getSpecialLeave());
        entity.setGatePass(dto.getGatePass());
        entity.setActualPresentDays(dto.getActualPresentDays());
        entity.setPaidDays(dto.getPaidDays());
        entity.setPresentPercentage(dto.getPresentPercentage());
        entity.setRemark(dto.getRemark());

        if (dto.getTeacher() != null) {
            TeacherBasicDTO t = dto.getTeacher();
            Teacher teacher = new Teacher();
            teacher.setTeacherId(t.getTeacherId());
            entity.setTeacher(teacher);

            if (t.getDepartmentId() != null) {
                Department dept = new Department();
                dept.setDepartmentId(t.getDepartmentId());
                entity.setDepartment(dept);
            }
        }

        return entity;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(StaffMonthlyReportDTO dto, @MappingTarget StaffMonthlyReport entity);

    default TeacherBasicDTO mapTeacherToBasicDTO(Teacher teacher) {
        if (teacher == null) return null;
        return TeacherBasicDTO.builder()
                .teacherId(teacher.getTeacherId())
                .fullName(
                        (teacher.getFirstName() != null ? teacher.getFirstName() : "") +
                                (teacher.getLastName() != null ? " " + teacher.getLastName() : "")
                )
                .designation(teacher.getDesignation())
                .departmentId(teacher.getDepartment() != null ? teacher.getDepartment().getDepartmentId() : null)
                .departmentName(teacher.getDepartment() != null ? teacher.getDepartment().getName() : null)
                .build();
    }
}
