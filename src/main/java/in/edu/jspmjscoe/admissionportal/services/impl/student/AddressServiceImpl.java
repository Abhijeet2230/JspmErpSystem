package in.edu.jspmjscoe.admissionportal.services.impl.student;

import in.edu.jspmjscoe.admissionportal.dtos.student.AddressDTO;
import in.edu.jspmjscoe.admissionportal.mappers.student.StudentAddressMapper;
import in.edu.jspmjscoe.admissionportal.model.student.Address;
import in.edu.jspmjscoe.admissionportal.model.student.Student;
import in.edu.jspmjscoe.admissionportal.repositories.student.AddressRepository;
import in.edu.jspmjscoe.admissionportal.repositories.student.StudentRepository;
import in.edu.jspmjscoe.admissionportal.services.student.AddressService;
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
    private final StudentAddressMapper studentAddressMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Address address = studentAddressMapper.toEntity(addressDTO);
        address.setStudent(student);

        return studentAddressMapper.toDTO(addressRepository.save(address));
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(studentAddressMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(studentAddressMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        studentAddressMapper.updateAddressFromDTO(addressDTO, address);

        return studentAddressMapper.toDTO(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new EntityNotFoundException("Address not found");
        }
        addressRepository.deleteById(id);
    }
}
