package in.edu.jspmjscoe.admissionportal.mappers.achievements.certificate;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.Certificate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    @Mapping(target = "studentAcademicYearId", source = "studentAcademicYear.studentAcademicYearId")
    CertificateDTO toDTO(Certificate certificate);

    @Mapping(target = "studentAcademicYear.studentAcademicYearId", source = "studentAcademicYearId")
    Certificate toEntity(CertificateDTO dto);
}
