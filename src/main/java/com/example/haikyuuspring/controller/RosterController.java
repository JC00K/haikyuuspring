package com.example.haikyuuspring.controller;

import com.example.haikyuuspring.controller.dto.CoachDTO;
import com.example.haikyuuspring.controller.dto.ManagementDTO;
import com.example.haikyuuspring.controller.dto.PlayerDTO;
import com.example.haikyuuspring.controller.dto.RosterDTO;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.services.RosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roster")
@RequiredArgsConstructor
public class RosterController {
    private final RosterService rosterService;

    @GetMapping("/get_roster_by_id/{rosterId}")
    public ResponseEntity<RosterDTO> getRosterById(@PathVariable Long rosterId) {
        return ResponseEntity.ok(rosterService.getRosterById(rosterId));
    }

    @GetMapping("/{rosterId}/get_players")
    public ResponseEntity<List<PlayerDTO>> getRosterPlayers(@PathVariable Long rosterId) {
        return ResponseEntity.ok(rosterService.getRosterPlayers(rosterId));
    }

    @GetMapping("/{rosterId}/get_coaches")
    public ResponseEntity<List<CoachDTO>> getRosterCoaches(@PathVariable Long rosterId) {
        return ResponseEntity.ok(rosterService.getRosterCoaches(rosterId));
    }

    @GetMapping("/{rosterId}/get_players_by_position/{position}")
    public ResponseEntity<List<PlayerDTO>> getPlayersByPosition(@PathVariable Long rosterId, @PathVariable Position position) {
        return ResponseEntity.ok(rosterService.findPlayersByPosition(rosterId, position));
    }

    @GetMapping("/{rosterId}/get_management")
    public ResponseEntity<List<ManagementDTO>> getRosterManagement(@PathVariable Long rosterId) {
        return ResponseEntity.ok(rosterService.getRosterManagement(rosterId));
    }

    @PatchMapping("/{rosterId}/remove_character_from_roster/{characterId}")
    public ResponseEntity<RosterDTO> removeCharacterFromRoster(@PathVariable Long rosterId, @PathVariable Long characterId) {
        return ResponseEntity.ok(rosterService.removeCharacterFromRoster(rosterId, characterId));
    }
}
