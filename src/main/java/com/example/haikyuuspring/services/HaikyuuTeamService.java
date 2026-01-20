package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.HaikyuuCharacterDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuTeamRosterDTO;
import com.example.haikyuuspring.model.entity.HaikyuuCharacter;
import com.example.haikyuuspring.model.entity.HaikyuuTeamRoster;
import com.example.haikyuuspring.repository.HaikyuuCharacterRepository;
import com.example.haikyuuspring.repository.HaikyuuSchoolRepository;
import com.example.haikyuuspring.repository.HaikyuuTeamRosterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class HaikyuuTeamService {
    private final HaikyuuTeamRosterRepository rosterRepository;
    private final HaikyuuSchoolRepository schoolRepository;
    private final HaikyuuCharacterRepository characterRepository;
    private final HaikyuuCharacterService characterService;


//    public List<HaikyuuCharacterDTO> findByPosition(Long schoolId, Position position) {
//        HaikyuuTeamRoster roster = rosterRepository.findById(schoolId).orElseThrow(() -> new RuntimeException("No Roster found with ID " + schoolId));
//        return roster.getRoster().stream().filter((p) -> p.getPosition() == position).toList();
//    }

    private List<HaikyuuCharacterDTO> getCoaches(HaikyuuTeamRoster roster) {
        List<HaikyuuCharacter> coaches = roster.getCoachesOnly();
        return characterService.mapListToDTO(coaches);
    }

    private List<HaikyuuCharacterDTO> getManagers(HaikyuuTeamRoster roster) {
        List<HaikyuuCharacter> managers = roster.getManagersOnly();
        return characterService.mapListToDTO(managers);
    }

    private List<HaikyuuCharacterDTO> getAdvisors(HaikyuuTeamRoster roster) {
        List<HaikyuuCharacter> advisors = roster.getAdvisorsOnly();
        return characterService.mapListToDTO(advisors);
    }


    private HaikyuuTeamRosterDTO convertToDTO(HaikyuuTeamRoster roster) {

        List<HaikyuuCharacter> players = roster.getPlayers();
        List<HaikyuuCharacter> staff = roster.getStaff();


        List<HaikyuuCharacterDTO> mappedPlayers = characterService.mapListToDTO(players);
        List<HaikyuuCharacterDTO> mappedStaff = characterService.mapListToDTO(staff);


        return new HaikyuuTeamRosterDTO(
                mappedPlayers,
                mappedStaff,
                roster.getTeamMotto()
        );
    }

}
