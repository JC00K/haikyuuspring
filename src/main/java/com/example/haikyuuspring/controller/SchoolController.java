package com.example.haikyuuspring.controller;


import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.controller.dto.SchoolDTO;
import com.example.haikyuuspring.controller.dto.SchoolLookupDTO;
import com.example.haikyuuspring.services.CharacterService;
import com.example.haikyuuspring.services.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final CharacterService characterService;
    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<List<SchoolLookupDTO>> getSchoolLookup() {
        return ResponseEntity.ok(schoolService.lookupForDropdown());
    }

    @GetMapping("/all")
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        return ResponseEntity.ok(schoolService.getAllSchools());
    }

    @GetMapping("/{school}/info")
    public ResponseEntity<SchoolDTO> getSchoolInfo(@PathVariable Long school) {
        return ResponseEntity.ok(schoolService.getSchoolInfo(school));
    }

    @GetMapping("/{school}/characters")
    public ResponseEntity<List<CharacterDTO>> getCharactersBySchool(@PathVariable String school) {
        return ResponseEntity.ok(characterService.findBySchool(school));
    }

    @GetMapping("/{prefecture}")
    public ResponseEntity<List<SchoolDTO>> getSchoolsByPrefecture(@PathVariable String prefecture) {
        return ResponseEntity.ok(schoolService.findByPrefecture(prefecture));
    }

    @PostMapping
    public ResponseEntity<SchoolDTO> createSchool(@Valid @RequestBody SchoolDTO schoolDTO) {
        return new ResponseEntity<>(schoolService.createSchool(schoolDTO), HttpStatus.CREATED);
    }

    @DeleteMapping
    public void deleteSchool(Long schoolId) {
        schoolService.deleteSchool(schoolId);
    }
}
