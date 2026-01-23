package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.*;
import com.example.haikyuuspring.model.entity.*;
import com.example.haikyuuspring.model.entity.Character;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.PlayerRepository;
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
    private final PlayerService playerService;
    private final CharacterService characterService;


//    public List<HaikyuuCharacterDTO> findByPosition(Long schoolId, Position position) {
//        HaikyuuTeamRoster roster = rosterRepository.findById(schoolId).orElseThrow(() -> new RuntimeException("No Roster found with ID " + schoolId));
//        return roster.getRoster().stream().filter((p) -> p.getPosition() == position).toList();
//    }

    private List<PlayerDTO> getPlayers(Roster roster) {
        List<Player> players = roster.getPlayers();
        return playerService.mapListToDTO(players);
    }

    private List<CoachDTO> getCoaches(Roster roster) {
        List<Coach> coaches = roster.getCoaches();
        return coachService.mapListToDTO(coaches);
    }

    private List<ManagementDTO> getManagement(Roster roster) {
        List<Management> advisors = roster.getAdvisorsOnly();
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
