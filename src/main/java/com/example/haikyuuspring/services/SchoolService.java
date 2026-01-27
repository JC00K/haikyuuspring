package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.*;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Player;
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
    private final PlayerService playerService;
    private final CoachService coachService;
    private final ManagementService managementService;
    private final RosterService rosterService;

    @Transactional
    public SchoolDTO createSchool(SchoolDTO schoolInfo) {
        if (schoolRepository.existsByName(schoolInfo.name())) {
            throw new ResourceDuplicateException(schoolInfo.name());
        }
        School school = new School();
        Roster roster = new Roster();


        school.setName(schoolInfo.name());
        school.setPrefecture(schoolInfo.prefecture());
        school.setRoster(roster);
        school.setMotto(schoolInfo.motto());
        school.setColors(schoolInfo.colors());
        school.setMascot(schoolInfo.mascot());
        school.setImgUrl(schoolInfo.imgUrl());

        roster.setSchool(school);
        School newSchool = schoolRepository.save(school);

        return convertSchoolToDto(newSchool);
    }

    @Transactional
    public void deleteSchool(Long schoolId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException(schoolId));
        schoolRepository.delete(school);
    }

    public SchoolDTO getSchoolInfo(Long schoolId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException(schoolId));
        return convertSchoolToDto(school);
    }

    public List<SchoolDTO> findByPrefecture(String prefecture) {
        List<School> schools = schoolRepository.findByPrefectureIgnoreCase(prefecture);

        if (schools.isEmpty()) {
            throw new ResourceNotFoundException(prefecture);
        }

        return schools.stream().map(this::convertSchoolToDto).toList();
    }

    public List<SchoolLookupDTO> lookupForDropdown() {
        return schoolRepository.findAllProjectedBy().stream()
                .map(view -> new SchoolLookupDTO(view.getId(), view.getName()))
                .toList();
    }

    private SchoolDTO convertSchoolToDto(School school) {
        List<PlayerDTO> players = null;
        List<CoachDTO> coaches = null;
        List<ManagementDTO> management = null;
        String motto = " ";

        if (school.getRoster() != null) {
            players = playerService.mapPlayerListToDTO(school.getRoster().getPlayers());
            coaches = coachService.mapCoachListToDTO(school.getRoster().getCoaches());
            management = managementService.mapManagementListToDTO(school.getRoster().getManagement());
            motto = school.getMotto();
        }

        return new SchoolDTO(
                school.getId(),
                school.getName(),
                school.getPrefecture(),
                new RosterDTO(players, coaches, management),
                motto,
                school.getColors(),
                school.getMascot(),
                school.getImgUrl()
        );
    }
}
