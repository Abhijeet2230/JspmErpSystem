package in.edu.jspmjscoe.admissionportal.services.impl;

import in.edu.jspmjscoe.admissionportal.dtos.AddressDTO;
import in.edu.jspmjscoe.admissionportal.mappers.AddressMapper;
import in.edu.jspmjscoe.admissionportal.model.Address;
import in.edu.jspmjscoe.admissionportal.model.Student;
import in.edu.jspmjscoe.admissionportal.repositories.AddressRepository;
import in.edu.jspmjscoe.admissionportal.repositories.StudentRepository;
import in.edu.jspmjscoe.admissionportal.services.AddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final StudentRepository studentRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Address address = addressMapper.toEntity(addressDTO);
        address.setStudent(student);

        return addressMapper.toDTO(addressRepository.save(address));
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        addressMapper.updateAddressFromDTO(addressDTO, address);

        return addressMapper.toDTO(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new EntityNotFoundException("Address not found");
        }
        addressRepository.deleteById(id);
    }
}
