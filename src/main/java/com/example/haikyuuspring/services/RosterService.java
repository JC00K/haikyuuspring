package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.*;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.*;
import com.example.haikyuuspring.model.entity.Character;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class RosterService {
    private final RosterRepository rosterRepository;
    private final CharacterRepository characterRepository;
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;
    private final CoachService coachService;
    private final CoachRepository coachRepository;
    private final ManagementService managementService;
    private final ManagementRepository managementRepository;

    @Transactional
    public RosterDTO removeCharacterFromRoster(Long rosterId, Long characterId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(rosterId));
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException(characterId));

        roster.removeCharacterFromRoster(character);
        rosterRepository.save(roster);
        return convertRosterToDTO(roster);
    }

    @Transactional
    public RosterDTO addPlayerToRoster(Long rosterId, Long playerId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(rosterId));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(playerId));

        roster.addPlayerToRoster(player);
        rosterRepository.save(roster);
        return convertRosterToDTO(roster);
    }

    @Transactional
    public RosterDTO addCoachToRoster(Long rosterId, Long coachId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(rosterId));
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new ResourceNotFoundException(coachId));

        roster.addCoachToRoster(coach);
        rosterRepository.save(roster);
        return convertRosterToDTO(roster);
    }

    @Transactional
    public RosterDTO addManagementToRoster(Long rosterId, Long managementId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(rosterId));
        Management management = managementRepository.findById(managementId)
                .orElseThrow(() -> new ResourceNotFoundException(managementId));

        roster.addManagementToRoster(management);
        rosterRepository.save(roster);
        return convertRosterToDTO(roster);
    }

    public List<RosterDTO> getAllRosters() {
        return mapRosterListToDTO(rosterRepository.findAll());
    }

    public RosterDTO getRosterById(Long rosterId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(rosterId));
        return convertRosterToDTO(roster);
    }

    public List<PlayerDTO> getRosterPlayers(Long rosterId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(rosterId));
        return playerService.mapPlayerListToDTO(roster.getPlayers());
    }


    public List<CoachDTO> getRosterCoaches(Long rosterId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(rosterId));
        return coachService.mapCoachListToDTO(roster.getCoaches());
    }

    public List<ManagementDTO> getRosterManagement(Long rosterId) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(rosterId));
        return managementService.mapManagementListToDTO(roster.getManagement());
    }

    public List<PlayerDTO> findPlayersByPosition(Long rosterId, Position position) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(rosterId));
        return roster.getPlayers().stream()
                .filter(player -> player.getPosition() == position)
                .map(playerService::convertPlayerToDTO)
                .toList();
    }

    public List<RosterDTO> mapRosterListToDTO(List<Roster> rosters) {
        return rosters.stream()
                .map(this::convertRosterToDTO)
                .toList();
    }

    private RosterDTO convertRosterToDTO(Roster roster) {
        List<PlayerDTO> mappedPlayers = playerService.mapPlayerListToDTO(roster.getPlayers());
        List<CoachDTO> mappedCoaches = coachService.mapCoachListToDTO(roster.getCoaches());
        List<ManagementDTO> mappedManagement = managementService.mapManagementListToDTO(roster.getManagement());

        return new RosterDTO(mappedPlayers, mappedCoaches, mappedManagement);
    }
}
