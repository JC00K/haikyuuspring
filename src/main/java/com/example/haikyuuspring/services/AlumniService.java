package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Alumni;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.CoachingStyle;
import com.example.haikyuuspring.repository.AlumniRepository;
import com.example.haikyuuspring.repository.CharacterRepository;

import com.example.haikyuuspring.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlumniService {
    private final AlumniRepository alumniRepository;
    private final CharacterRepository characterRepository;
    private final SchoolRepository schoolRepository;

    @Transactional
    public AlumniDTO createAlumni(AlumniDTO alumniInfo) {
        if (characterRepository.existsByName(alumniInfo.name())) {
            throw new ResourceDuplicateException(alumniInfo.name());
        }
        Alumni alumni = new Alumni();
        alumni.setName(alumniInfo.name());
        alumni.setImgUrl(alumniInfo.imgUrl());
        alumni.setHeight(alumniInfo.height());
        alumni.setAge(alumniInfo.age());
        alumni.setRole(alumniInfo.role());

        if (alumniInfo.schoolId() != null) {
            School school = schoolRepository.findById(alumniInfo.schoolId()).orElseThrow(() -> new ResourceNotFoundException(alumniInfo.schoolId()));
            alumni.setSchool(school);
        }

        if (Boolean.TRUE.equals(alumniInfo.formerPlayer())) {
            if (alumniInfo.position() == null) {
            throw new RuntimeException("Former players must have a position");
            }
            alumni.setFormerPlayer(true);
            alumni.setPosition(alumniInfo.position());
            alumni.setJerseyNumber(alumniInfo.jerseyNumber());
        } else {
            if (alumniInfo.position() != null || alumniInfo.jerseyNumber() != null) {
                throw new IllegalArgumentException("Non-Former players cannot have a position or jersey number");
            }
            alumni.setFormerPlayer(false);
            alumni.setPosition(null);
            alumni.setJerseyNumber(null);
        }

        if (Boolean.TRUE.equals(alumniInfo.formerCoach())) {
            alumni.setFormerCoach(true);
            alumni.setCoachingStyle(alumniInfo.coachingStyle());
        } else {
            alumni.setFormerCoach(false);
            alumni.setCoachingStyle(CoachingStyle.NONCOACH);
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

    public List<AlumniDTO> findAlumniBySchoolId(Long schoolId) {
        return findAllAlumni().stream().filter((alumni) -> Objects.equals(alumni.schoolId(), schoolId)).toList();
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
                alumni.getHeight(),
                alumni.getAge(),
                alumni.getRole(),
                Optional.ofNullable(alumni.getSchool()).map(School::getId).orElse(null),
                Optional.ofNullable(alumni.getSchool()).map(School::getName).orElse(null),
                alumni.getImgUrl(),
                alumni.getFormerPlayer(),
                alumni.getPosition(),
                alumni.getJerseyNumber(),
                alumni.getFormerCoach(),
                alumni.getCoachingStyle()
        );
    }
}