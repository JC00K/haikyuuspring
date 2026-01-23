package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.controller.dto.RosterDTO;
import com.example.haikyuuspring.model.entity.Character;
import com.example.haikyuuspring.model.entity.Roster;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.repository.RosterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class RosterService {
    private final RosterRepository rosterRepository;
    private final SchoolRepository schoolRepository;
    private final CharacterRepository characterRepository;
    private final CharacterService characterService;


//    public List<HaikyuuCharacterDTO> findByPosition(Long schoolId, Position position) {
//        HaikyuuTeamRoster roster = rosterRepository.findById(schoolId).orElseThrow(() -> new RuntimeException("No Roster found with ID " + schoolId));
//        return roster.getRoster().stream().filter((p) -> p.getPosition() == position).toList();
//    }

    private List<CharacterDTO> getCoaches(Roster roster) {
        List<Character> coaches = roster.getCoachesOnly();
        return characterService.mapListToDTO(coaches);
    }

    private List<CharacterDTO> getManagers(Roster roster) {
        List<Character> managers = roster.getManagersOnly();
        return characterService.mapListToDTO(managers);
    }

    private List<CharacterDTO> getAdvisors(Roster roster) {
        List<Character> advisors = roster.getAdvisorsOnly();
        return characterService.mapListToDTO(advisors);
    }


    private RosterDTO convertToDTO(Roster roster) {

        List<Character> players = roster.getPlayers();
        List<Character> staff = roster.getStaff();


        List<CharacterDTO> mappedPlayers = characterService.mapListToDTO(players);
        List<CharacterDTO> mappedStaff = characterService.mapListToDTO(staff);


        return new RosterDTO(
                mappedPlayers,
                mappedStaff,
                roster.getTeamMotto()
        );
    }

}
