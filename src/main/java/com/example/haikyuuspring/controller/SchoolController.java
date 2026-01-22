package com.example.haikyuuspring.controller;


import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.controller.dto.SchoolDTO;
import com.example.haikyuuspring.controller.dto.SchoolLookupDTO;
import com.example.haikyuuspring.services.CharacterService;
import com.example.haikyuuspring.services.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final CharacterService haikyuuCharacterService;
    private final SchoolService haikyuuSchoolService;

    @GetMapping
    public ResponseEntity<List<SchoolLookupDTO>> getSchoolLookup() {
        return ResponseEntity.ok(haikyuuSchoolService.lookupForDropdown());
    }

    @GetMapping("/{school}/info")
    public ResponseEntity<SchoolDTO> getSchoolInfo(@PathVariable Long school) {
        SchoolDTO schoolInfo = haikyuuSchoolService.getSchoolInfo(school);
        return ResponseEntity.ok(schoolInfo);
    }

    @GetMapping("/{school}/characters")
    public ResponseEntity<List<CharacterDTO>> getCharactersBySchool(@PathVariable String school) {
        List<CharacterDTO> characters = haikyuuCharacterService.findBySchool(school);
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/{prefecture}")
    public ResponseEntity<List<SchoolDTO>> getSchoolsByPrefecture(@PathVariable String prefecture) {
        List<SchoolDTO> characters = haikyuuSchoolService.findByPrefecture(prefecture);
        return ResponseEntity.ok(characters);
    }

    @PostMapping
    public ResponseEntity<SchoolDTO> createSchool(@Valid @RequestBody SchoolDTO schoolDTO) {
        return new ResponseEntity<>(haikyuuSchoolService.createSchool(schoolDTO), HttpStatus.CREATED);
    }

    @DeleteMapping
    public void deleteSchool(Long schoolId) {
        haikyuuSchoolService.deleteSchool(schoolId);
    }
}
