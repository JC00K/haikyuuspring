package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.*;
import com.example.haikyuuspring.exception.DuplicateSchoolException;
import com.example.haikyuuspring.exception.NoPrefectureException;
import com.example.haikyuuspring.exception.SchoolNotFoundException;
import com.example.haikyuuspring.model.entity.HaikyuuSchool;
import com.example.haikyuuspring.model.entity.HaikyuuTeamRoster;
import com.example.haikyuuspring.repository.HaikyuuCharacterRepository;
import com.example.haikyuuspring.repository.HaikyuuSchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HaikyuuSchoolService {
    private final HaikyuuSchoolRepository schoolRepository;
    private final HaikyuuCharacterService characterService;

    @Transactional
    public HaikyuuSchoolDTO createSchool(HaikyuuSchoolDTO schoolInfo) {
        if (schoolRepository.existsByName(schoolInfo.name())) {
            throw new DuplicateSchoolException(schoolInfo.name());
        }
        HaikyuuSchool school = new HaikyuuSchool();
        HaikyuuTeamRoster team = new HaikyuuTeamRoster();

        team.setTeamMotto(schoolInfo.team().motto());
        school.setColors(schoolInfo.colors());
        school.setName(schoolInfo.name());
        school.setPrefecture(schoolInfo.prefecture());
        school.setTeam(team);
        school.setMascot(schoolInfo.mascot());
        school.setImgUrl(schoolInfo.imgUrl());

        team.setSchool(school);
        HaikyuuSchool newSchool = schoolRepository.save(school);

        return convertToDto(newSchool);
    }

    public HaikyuuSchoolDTO getSchoolInfo(Long schoolId) {
        HaikyuuSchool school = schoolRepository.findById(schoolId).orElseThrow(() -> new RuntimeException("School not found"));
        return convertToDto(school);
    }

    public List<HaikyuuSchoolDTO> findByPrefecture(String prefecture) {
        List<HaikyuuSchool> schools = schoolRepository.findByPrefectureIgnoreCase(prefecture);

        if (schools.isEmpty()) {
            throw new NoPrefectureException(prefecture);
        }

        return schools.stream().map(this::convertToDto).toList();
    }

    public List<HaikyuuSchoolLookupDTO> lookupForDropdown() {
        return schoolRepository.findAllProjectedBy().stream()
                .map(view -> new HaikyuuSchoolLookupDTO(view.getId(), view.getName()))
                .toList();
    }

    private HaikyuuSchoolDTO convertToDto(HaikyuuSchool school) {
        List<HaikyuuCharacterDTO> roster = null;
        String motto = " ";
        if (school.getTeam() != null) {
            roster = school.getTeam().getRoster().stream().map(characterService::convertToDTO).toList();
            motto = school.getTeam().getTeamMotto();
        }

        return new HaikyuuSchoolDTO(
                school.getId(),
                school.getName(),
                school.getPrefecture(),
                new HaikyuuTeamRosterDTO(roster, motto),
                motto,
                school.getMascot(),
                school.getImgUrl()
        );
    }

    @Transactional
    public void deleteSchool(Long schoolId) {
        HaikyuuSchool school = schoolRepository.findById(schoolId).orElseThrow(() -> new SchoolNotFoundException(schoolId));
        schoolRepository.delete(school);
    }
}
