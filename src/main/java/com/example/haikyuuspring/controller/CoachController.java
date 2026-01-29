package com.example.haikyuuspring.controller;


import com.example.haikyuuspring.controller.dto.CoachDTO;
import com.example.haikyuuspring.services.CoachService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coaches")
@RequiredArgsConstructor
public class CoachController {
    private final CoachService coachService;

    @GetMapping
    public ResponseEntity<List<CoachDTO>> getAllCoaches() {
        return ResponseEntity.ok(coachService.findALlCoaches());
    }

    @PostMapping
    public ResponseEntity<CoachDTO> createCoach(@Valid @RequestBody CoachDTO coachDTO) {
        return new ResponseEntity<>(coachService.createCoach(coachDTO), HttpStatus.CREATED);
    }
}
