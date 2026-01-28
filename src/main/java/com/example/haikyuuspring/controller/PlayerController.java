package com.example.haikyuuspring.controller;

import com.example.haikyuuspring.controller.dto.PlayerDTO;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.services.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return ResponseEntity.ok(playerService.findAllPlayers());
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<PlayerDTO>> getPosition(@PathVariable Position position) {
        return ResponseEntity.ok(playerService.findByPosition(position));
    }

    @GetMapping("/jersey_number/{jerseyNumber}")
    public ResponseEntity<List<PlayerDTO>> getJerseyNumber(@PathVariable Integer jerseyNumber) {
        return ResponseEntity.ok(playerService.findByJerseyNumber(jerseyNumber));
    }

    @GetMapping("/find_by_year_and_position/{year}_{position}")
    public ResponseEntity<List<PlayerDTO>> getYearAndPosition(@PathVariable Year year, @PathVariable Position position) {
        return ResponseEntity.ok(playerService.findByYearAndPosition(year, position));
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        return new ResponseEntity<>(playerService.createPlayer(playerDTO), HttpStatus.CREATED);
    }
}
