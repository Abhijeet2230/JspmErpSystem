package in.edu.jspmjscoe.admissionportal.services.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, Long studentId);
    AddressDTO getAddressById(Long id);
    List<AddressDTO> getAllAddresses();
    AddressDTO updateAddress(Long id, AddressDTO addressDTO);
    void deleteAddress(Long id);
}
