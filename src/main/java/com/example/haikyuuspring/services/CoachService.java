package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.controller.dto.CoachDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Coach;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.CoachRole;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.CoachRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoachService {
    private final CoachRepository coachRepository;
    private final CharacterRepository characterRepository;
    private final SchoolRepository schoolRepository;
    private final CharacterService characterService;

    @Transactional
    public CoachDTO createCoach(CoachDTO coachInfo) {
        if (characterRepository.existsByName(coachInfo.name())) {
            throw new ResourceDuplicateException(coachInfo.name());
        }

        Coach coach = new Coach();

        coach.setName(coachInfo.name());
        coach.setHeight(coachInfo.height());
        coach.setAge(coachInfo.age());
        coach.setRole(coachInfo.role());
        coach.setIsRetired(coachInfo.isRetired());
        coach.setImgUrl(coachInfo.imgUrl());
        coach.setCoachRole(coachInfo.coachRole());
        coach.setCoachingStyle(coachInfo.coachingStyle());


        if (coachInfo.schoolId() != null) {
            School school = schoolRepository.findById(coachInfo.schoolId()).orElseThrow(() -> new ResourceNotFoundException(coachInfo.schoolId()));
            coach.setSchool(school);
        }

        Coach newCoach = coachRepository.save(coach);

        return convertCoachToDTO(newCoach);
    }

    public List<CoachDTO> findALlCoaches() {
        return mapCoachListToDTO(coachRepository.findAll());
    }

    public CoachDTO findById(Long id) {
        return coachRepository.findById(id)
                .map(this::convertCoachToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }


    public List<CoachDTO> mapCoachListToDTO(List<Coach> coaches) {
        return coaches.stream()
                .map(this::convertCoachToDTO)
                .toList();
    }

    private CoachDTO convertCoachToDTO(Coach coach) {


        return new CoachDTO(
                coach.getId(),
                coach.getName(),
                coach.getHeight(),
                coach.getAge(),
                coach.getRole(),
                Optional.ofNullable(coach.getSchool()).map(School::getId).orElse(null),
                Optional.ofNullable(coach.getSchool()).map(School::getName).orElse(null),
                coach.getImgUrl(),
                coach.getIsRetired(),
                coach.getCoachRole(),
                coach.getCoachingStyle()
        );
    }
}
