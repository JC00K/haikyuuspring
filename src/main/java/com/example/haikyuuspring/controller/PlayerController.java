package com.example.haikyuuspring.controller;

import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.controller.dto.PlayerDTO;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("position/{position}")
    public ResponseEntity<List<PlayerDTO>> getPlayersByPosition(@PathVariable Position position) {
        return ResponseEntity.ok(playerService.findByPosition(position));
    }

    @GetMapping("jerseyNumber/{jerseyNumber}")
    public ResponseEntity<List<PlayerDTO>> getPlayersByJerseyNumber(@PathVariable Integer jerseyNumber) {
        return ResponseEntity.ok(playerService.findByJerseyNumber(jerseyNumber));
    }
}
