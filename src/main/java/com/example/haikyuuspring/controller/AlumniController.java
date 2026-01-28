package com.example.haikyuuspring.controller;

import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.services.AlumniService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumni")
@RequiredArgsConstructor
public class AlumniController {
    private final AlumniService alumniService;

    @GetMapping
    public ResponseEntity<List<AlumniDTO>> getAllAlumni() {
        return ResponseEntity.ok(alumniService.findAllAlumni());
    }

    @GetMapping("/all_former_players")
    public ResponseEntity<List<AlumniDTO>> getAllFormerPlayers() {
        return ResponseEntity.ok(alumniService.findAllFormerPlayers());
    }

    @GetMapping("/get_by_school_id/{schoolId}")
    public ResponseEntity<List<AlumniDTO>> getAlumniBySchoolId(@Valid Long schoolId) {
        return ResponseEntity.ok(alumniService.findAlumniBySchoolId(schoolId));
    }

    @PostMapping
    public ResponseEntity<AlumniDTO> createAlumni(@Valid @RequestBody AlumniDTO alumniDTO) {
        return new ResponseEntity<>(alumniService.createAlumni(alumniDTO), HttpStatus.CREATED);
    }

}
