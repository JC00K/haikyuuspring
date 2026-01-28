package com.example.haikyuuspring.controller;


import com.example.haikyuuspring.controller.dto.CoachDTO;
import com.example.haikyuuspring.services.CoachService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coaches")
@RequiredArgsConstructor
public class CoachController {
    private final CoachService coachService;

    @PostMapping
    public ResponseEntity<CoachDTO> createCoach(@Valid @RequestBody CoachDTO coachDTO) {
        return new ResponseEntity<>(coachService.createCoach(coachDTO), HttpStatus.CREATED);
    }
}
