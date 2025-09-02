package in.edu.jspmjscoe.admissionportal.services;

import in.edu.jspmjscoe.admissionportal.dtos.AddressDTO;
import in.edu.jspmjscoe.admissionportal.model.Address;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, Long studentId);
    AddressDTO getAddressById(Long id);
    List<AddressDTO> getAllAddresses();
    AddressDTO updateAddress(Long id, AddressDTO addressDTO);
    void deleteAddress(Long id);
}
