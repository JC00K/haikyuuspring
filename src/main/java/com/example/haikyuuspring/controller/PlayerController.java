package com.example.haikyuuspring.controller;

import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.controller.dto.PlayerDTO;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.services.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.findById(id));
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

    @GetMapping("/find_by_height_greater_than/{height}")
    public ResponseEntity<List<PlayerDTO>> getPlayersGreaterThanHeight(@PathVariable Double height) {
        return ResponseEntity.ok(playerService.findByHeightGreaterThan(height));
    }

    @GetMapping("/find_by_height_less_than/{height}")
    public ResponseEntity<List<PlayerDTO>> getPlayersLessThanHeight(@PathVariable Double height) {
        return ResponseEntity.ok(playerService.findByHeightLessThan(height));
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        return new ResponseEntity<>(playerService.createPlayer(playerDTO), HttpStatus.CREATED);
    }
}
