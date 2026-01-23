package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.*;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.entity.Roster;
import com.example.haikyuuspring.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private final CharacterService characterService;
    private final RosterService teamService;

    @Transactional
    public SchoolDTO createSchool(SchoolDTO schoolInfo) {
        if (schoolRepository.existsByName(schoolInfo.name())) {
            throw new ResourceDuplicateException(schoolInfo.name());
        }
        School school = new School();
        Roster team = new Roster();

        team.setTeamMotto(schoolInfo.team().motto());
        school.setColors(schoolInfo.colors());
        school.setName(schoolInfo.name());
        school.setPrefecture(schoolInfo.prefecture());
        school.setTeam(team);
        school.setMascot(schoolInfo.mascot());
        school.setImgUrl(schoolInfo.imgUrl());

        team.setSchool(school);
        School newSchool = schoolRepository.save(school);

        return convertToDto(newSchool);
    }

    @Transactional
    public void deleteSchool(Long schoolId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException(schoolId));
        schoolRepository.delete(school);
    }

    public SchoolDTO getSchoolInfo(Long schoolId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException(schoolId));
        return convertToDto(school);
    }

    public List<SchoolDTO> findByPrefecture(String prefecture) {
        List<School> schools = schoolRepository.findByPrefectureIgnoreCase(prefecture);

        if (schools.isEmpty()) {
            throw new ResourceNotFoundException(prefecture);
        }

        return schools.stream().map(this::convertToDto).toList();
    }

    public List<SchoolLookupDTO> lookupForDropdown() {
        return schoolRepository.findAllProjectedBy().stream()
                .map(view -> new SchoolLookupDTO(view.getId(), view.getName()))
                .toList();
    }

    private SchoolDTO convertToDto(School school) {
        List<CharacterDTO> players = null;
        List<CharacterDTO> staff = null;
        String motto = " ";


        if (school.getTeam() != null) {
            players = school.getTeam().getPlayers().stream().map(characterService::convertToDTO).toList();
            staff = school.getTeam().getStaff().stream().map(characterService::convertToDTO).toList();
            motto = school.getTeam().getTeamMotto();
        }

        return new SchoolDTO(
                school.getId(),
                school.getName(),
                school.getPrefecture(),
                new RosterDTO(players, staff, motto),
                motto,
                school.getMascot(),
                school.getImgUrl()
        );
    }

}
