package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.HaikyuuSchoolDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuSchoolLookupDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuTeamDetailsDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuTeamRosterDTO;
import com.example.haikyuuspring.exception.DuplicateSchoolException;
import com.example.haikyuuspring.exception.NoPrefectureException;
import com.example.haikyuuspring.model.entity.HaikyuuSchool;
import com.example.haikyuuspring.model.entity.HaikyuuTeamRoster;
import com.example.haikyuuspring.repository.HaikyuuSchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HaikyuuSchoolService {
    private final HaikyuuSchoolRepository schoolRepository;

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
        school.setColors(schoolInfo.colors());
        school.setTeam(team);
        school.setMascot(schoolInfo.mascot());
        school.setImgUrl(schoolInfo.imgUrl());

        HaikyuuSchool newSchool = schoolRepository.save(school);

        return new HaikyuuSchoolDTO(newSchool.getId(), newSchool.getName(), newSchool.getPrefecture(), new HaikyuuTeamRosterDTO(
                newSchool.getTeam().getTeamMotto()
        ), newSchool.getMascot(),newSchool.getImgUrl());
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
        return new HaikyuuSchoolDTO(
                school.getId(),
                school.getName(),
                school.getPrefecture(),
                new HaikyuuTeamRosterDTO(school.getTeam().getTeamMotto(), school.getTeam().getColors()),
                school.getMascot(),
                school.getImgUrl()
        );
    }
}
