package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.HaikyuuCharacterDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuTeamRosterDTO;
import com.example.haikyuuspring.model.entity.HaikyuuCharacter;
import com.example.haikyuuspring.model.entity.HaikyuuSchool;
import com.example.haikyuuspring.model.entity.HaikyuuTeamRoster;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.repository.HaikyuuCharacterRepository;
import com.example.haikyuuspring.repository.HaikyuuSchoolRepository;
import com.example.haikyuuspring.repository.HaikyuuTeamRosterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HaikyuuTeamService {
    private final HaikyuuTeamRosterRepository rosterRepository;
    private final HaikyuuSchoolRepository schoolRepository;
    private final HaikyuuCharacterRepository characterRepository;

    public List<HaikyuuCharacterDTO> findByPosition(Long schoolId, Position position) {
        HaikyuuTeamRoster roster = rosterRepository.findById(schoolId).orElseThrow(() -> new RuntimeException("No Roster found with ID " + schoolId));
        return roster.getRoster().stream().filter((p) -> p.getPosition() == position).toList();
    }

    private HaikyuuTeamRosterDTO convertToDTO(HaikyuuTeamRoster roster) {

        return new HaikyuuTeamRosterDTO(
                character.getId(),
                character.getName(),
                Optional.ofNullable(character.getSchool()).map(HaikyuuSchool::getId).orElse(null),
                Optional.ofNullable(character.getSchool()).map(HaikyuuSchool::getName).orElse(null),
                character.getRole(),
                character.getPosition(),
                character.getAge(),
                character.getYear(),
                character.getHeight(),
                character.getImgUrl()
        );
    }
}
