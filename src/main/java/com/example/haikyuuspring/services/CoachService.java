package com.example.haikyuuspring.services;


import com.example.haikyuuspring.controller.dto.CoachDTO;
import com.example.haikyuuspring.controller.dto.ManagementDTO;
import com.example.haikyuuspring.controller.dto.PlayerDTO;
import com.example.haikyuuspring.controller.dto.RosterDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Coach;
import com.example.haikyuuspring.model.entity.Roster;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.CoachRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
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

    public CoachDTO createCoach(CoachDTO coachInfo) {
        if (characterRepository.existsByName(coachInfo.name())) {
            throw new ResourceDuplicateException(coachInfo.name());
        }
        Coach coach = new Coach();

        if (coachInfo.schoolId() != null) {
            School school = schoolRepository.findById(coachInfo.schoolId()).orElseThrow(() -> new ResourceNotFoundException(coachInfo.schoolId()));
            coach.setSchool(school);
        }

        Coach newCoach = coachRepository.save(coach);

        return convertCoachToDTO(newCoach);
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
                Optional.ofNullable(coach.getSchool()).map(School::getId).orElse(null),
                Optional.ofNullable(coach.getSchool()).map(School::getName).orElse(null),
                coach.getRole(),
                coach.getAge(),
                coach.getCoachingStyle(),
                coach.getCoachRole()
        );
    }
}
