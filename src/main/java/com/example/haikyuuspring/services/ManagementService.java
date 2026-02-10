package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.controller.dto.ManagementDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Management;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.ManagementRole;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.ManagementRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagementService {
    private final ManagementRepository managementRepository;
    private final CharacterRepository characterRepository;
    private final SchoolRepository schoolRepository;

    @Transactional
    public ManagementDTO createManagement(ManagementDTO managementInfo) {
        if (characterRepository.existsByName(managementInfo.name())) {
            throw new ResourceDuplicateException(managementInfo.name());
        }
        Management management = new Management();

        management.setName(managementInfo.name());
        management.setHeight(managementInfo.height());
        management.setAge(managementInfo.age());
        management.setYear(managementInfo.year());
        management.setRole(managementInfo.role());
        management.setImgUrl(managementInfo.imgUrl());
        management.setManagementRole(managementInfo.managementRole());


        if (managementInfo.schoolId() != null) {
            School school = schoolRepository.findById(managementInfo.schoolId()).orElseThrow(() -> new ResourceNotFoundException(managementInfo.schoolId()));
            management.setSchool(school);
        }

        Management newManagement = managementRepository.save(management);

        return convertManagementToDTO(newManagement);
    }

    public List<ManagementDTO> findAllManagement() {
        return mapManagementListToDTO(managementRepository.findAll());
    }

    public ManagementDTO findById(Long id) {
        return managementRepository.findById(id)
                .map(this::convertManagementToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<ManagementDTO> findBySchoolId(Long schoolId) {
        return findAllManagement().stream().filter((management) -> Objects.equals(management.schoolId(), schoolId)).toList();
    }

    public List<ManagementDTO> findByManagementRole(ManagementRole role) {
        return findAllManagement().stream().filter((manager) -> manager.managementRole() == role).toList();
    }

    public List<ManagementDTO> mapManagementListToDTO(List<Management> management) {
        return management.stream()
                .map(this::convertManagementToDTO)
                .toList();
    }

    private ManagementDTO convertManagementToDTO(Management management) {


        return new ManagementDTO(
                management.getId(),
                management.getName(),
                management.getHeight(),
                management.getAge(),
                management.getYear(),
                management.getRole(),
                Optional.ofNullable(management.getSchool()).map(School::getId).orElse(null),
                Optional.ofNullable(management.getSchool()).map(School::getName).orElse(null),
                management.getImgUrl(),
                management.getManagementRole()
        );
    }
}
