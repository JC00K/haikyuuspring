package com.example.haikyuuspring.controller;


import com.example.haikyuuspring.controller.dto.HaikyuuCharacterDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuSchoolDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuSchoolLookupDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuSchoolLookupView;
import com.example.haikyuuspring.services.HaikyuuCharacterService;
import com.example.haikyuuspring.services.HaikyuuSchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
public class HaikyuuSchoolController {
    private final HaikyuuCharacterService haikyuuCharacterService;
    private final HaikyuuSchoolService haikyuuSchoolService;

    @GetMapping
    public ResponseEntity<List<HaikyuuSchoolLookupDTO>> getSchoolLookup() {
        return ResponseEntity.ok(haikyuuSchoolService.lookupForDropdown());
    }

    @GetMapping("/{school}/info")
    public ResponseEntity<HaikyuuSchoolDTO> getSchoolInfo(@PathVariable Long school) {
        HaikyuuSchoolDTO schoolInfo = haikyuuSchoolService.getSchoolInfo(school);
        return ResponseEntity.ok(schoolInfo);
    }

    @GetMapping("/{school}/characters")
    public ResponseEntity<List<HaikyuuCharacterDTO>> getCharactersBySchool(@PathVariable String school) {
        List<HaikyuuCharacterDTO> characters = haikyuuCharacterService.findBySchool(school);
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/{prefecture}")
    public ResponseEntity<List<HaikyuuSchoolDTO>> getSchoolsByPrefecture(@PathVariable String prefecture) {
        List<HaikyuuSchoolDTO> characters = haikyuuSchoolService.findByPrefecture(prefecture);
        return ResponseEntity.ok(characters);
    }

    @PostMapping
    public ResponseEntity<HaikyuuSchoolDTO> createSchool(@Valid @RequestBody HaikyuuSchoolDTO schoolDTO) {
        return new ResponseEntity<>(haikyuuSchoolService.createSchool(schoolDTO), HttpStatus.CREATED);
    }
}
