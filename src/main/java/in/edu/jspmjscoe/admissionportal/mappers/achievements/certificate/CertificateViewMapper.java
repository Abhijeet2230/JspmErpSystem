package in.edu.jspmjscoe.admissionportal.mappers.achievements.certificate;

import in.edu.jspmjscoe.admissionportal.dtos.achievements.certificate.CertificateViewDTO;
import in.edu.jspmjscoe.admissionportal.model.achievements.Certificate;
import in.edu.jspmjscoe.admissionportal.services.impl.achievements.MinioStorageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CertificateViewMapper {

    @Mapping(target = "fileUrl", source = "minioObjectKey")
    public abstract CertificateViewDTO toDTO(Certificate certificate);

}
