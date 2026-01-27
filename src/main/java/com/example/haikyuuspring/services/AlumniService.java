package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Alumni;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.repository.AlumniRepository;
import com.example.haikyuuspring.repository.CharacterRepository;

import com.example.haikyuuspring.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlumniService {
    private final AlumniRepository alumniRepository;
    private final CharacterRepository characterRepository;
    private final SchoolRepository schoolRepository;

    public AlumniDTO createAlumni(AlumniDTO alumniInfo) {
        if (characterRepository.existsByName(alumniInfo.name())) {
            throw new ResourceDuplicateException(alumniInfo.name());
        }
        Alumni alumni = new Alumni();

        if (alumniInfo.schoolId() != null) {
            School school = schoolRepository.findById(alumniInfo.schoolId()).orElseThrow(() -> new ResourceNotFoundException(alumniInfo.schoolId()));
            alumni.setSchool(school);
        }

        Alumni newAlumni = alumniRepository.save(alumni);

        return convertAlumniToDTO(newAlumni);
    }

    public List<AlumniDTO> findAllAlumni() {
        return mapAlumniListToDTO(alumniRepository.findAll());
    }

    public List<AlumniDTO> findAllFormerPlayers() {
        return findAllAlumni().stream().filter((alumni) -> alumni.formerPlayer() == true).toList();
    }

    public List<AlumniDTO> mapAlumniListToDTO(List<Alumni> alumni) {
        return alumni.stream()
                .map(this::convertAlumniToDTO)
                .toList();
    }

    private AlumniDTO convertAlumniToDTO(Alumni alumni) {


        return new AlumniDTO(
                alumni.getId(),
                alumni.getName(),
                Optional.ofNullable(alumni.getSchool()).map(School::getId).orElse(null),
                Optional.ofNullable(alumni.getSchool()).map(School::getName).orElse(null),
                alumni.getFormerPlayer(),
                alumni.getPosition(),
                alumni.getJerseyNumber(),
                alumni.getImgUrl()
        );
    }
}